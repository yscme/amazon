package cn.yscme.www.pageProcessor;

import java.util.Random;

import org.springframework.stereotype.Component;

import cn.yscme.www.bean.Amazon;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;
@Component
public class AmazonCOM implements PageProcessor{
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(6000);

	private String [] ua = {
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1",
            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; Intel Mac OS X 10.6; rv:7.0.1) Gecko/20100101 Firefox/7.0.1",
            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36 OPR/18.0.1284.68",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)",
            "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0)",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0.1) Gecko/20100101 Firefox/4.0.1",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:7.0.1) Gecko/20100101 Firefox/7.0.1",
            "Opera/9.80 (Macintosh; Intel Mac OS X 10.9.1) Presto/2.12.388 Version/12.16",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36 OPR/18.0.1284.68",
            "Mozilla/5.0 (iPad; CPU OS 7_0 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) CriOS/30.0.1599.12 Mobile/11A465 Safari/8536.25",
            "Mozilla/5.0 (iPad; CPU OS 8_0 like Mac OS X) AppleWebKit/600.1.3 (KHTML, like Gecko) Version/8.0 Mobile/12A4345d Safari/600.1.4",
            "Mozilla/5.0 (iPad; CPU OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A501 Safari/9537.53"
    };
	@Override
	public void process(Page page) {
		//抓取内容
		page.putField("images", page.getHtml().css("div#altImages ul li span span span span img","src").all());
		page.putField("image", page.getHtml().css("div#imgTagWrapperId img","src").get());
		page.putField("title", page.getHtml().css("span#productTitle","text").get());
		page.putField("price", page.getHtml().css("span#priceblock_ourprice","text").get());
		page.putField("description", page.getHtml().css("div#productDescription p","text").all());
		page.putField("inventory", page.getHtml().css("div#availability span.a-size-medium.a-color-success","text").get());
		page.putField("score", page.getHtml().css("span#acrPopover","title").get());
		page.putField("ratings", page.getHtml().css("span#acrCustomerReviewText","text").get());
		page.putField("url",page.getRequest().getUrl());
		if (page.getResultItems().get("price")==null){
			//skip this page
			page.putField("price", page.getHtml().css("span#price_inside_buybox","text").get());
			if (page.getResultItems().get("price")==null) {
				page.putField("price", page.getHtml().css("span#price","text").get());
			}
		}
		if(page.getResultItems().get("image")==null)
			page.putField("image", page.getHtml().css("div#img-canvas img","src").get());
			if(page.getResultItems().get("image")==null)
				page.putField("image", page.getHtml().css("div#audibleimageblock_feature_div img","src").get());
				if(page.getResultItems().get("image")==null)
					page.putField("image", page.getHtml().css("div#ebooks-img-canvas img:not(#ebooksSitbLogoImg)","src").get());
		if (page.getResultItems().get("title")==null){
			page.setSkip(true);
		}else {
			//可自行添加其他输出方式 redis储存
			Amazon.list.add(page.getResultItems().getAll());
		}
		//商品列表正则过滤
		page.addTargetRequests(page.getHtml().css("div.s-main-slot.s-result-list.s-search-results.sg-row").links()
				.regex("^https:\\/\\/www\\.amazon\\.com\\/.*dp\\/.+\\/ref=.+\\d\\?.+&qid=.+&sr=(?!.+#.+).+$").all());
	}

	@Override
	public Site getSite() {
		Random index = new Random();
		String u = ua[Math.abs(index.nextInt()%16)];
        //随机调用ua，处理亚马逊流量验证
		site.setUserAgent(u);
		return site;
	}
	
	public void run(String search,int page,int thread) {
		for(int i=1;i<=page;i++) {
			Spider.create(new AmazonCOM())
			.addUrl("https://www.amazon.com/s?k="+search+"&page="+i)
			.setScheduler(new QueueScheduler().setDuplicateRemover(new HashSetDuplicateRemover())) //url去重
			.thread(thread).run();
		}
	}
}
