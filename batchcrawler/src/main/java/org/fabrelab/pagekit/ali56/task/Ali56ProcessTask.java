package org.fabrelab.pagekit.ali56.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.fabrelab.pagekit.ali56.Ali56Crawler;
import org.fabrelab.pagekit.ali56.model.Ali56Network;
import org.fabrelab.pagekit.ali56.model.Ali56Route;
import org.fabrelab.pagekit.common.TaskPool;
import org.fabrelab.pagekit.common.model.Page;
import org.fabrelab.pagekit.common.task.ProcessTask;
import org.fabrelab.pagekit.common.task.StoreTask;
import org.springframework.util.StringUtils;

public class Ali56ProcessTask extends ProcessTask {

	Pattern routesPattern = Pattern.compile("/corp-[^\\-]+\\/search_routes\\-p(\\d+).htm");

	Pattern networksPattern = Pattern.compile("/corp-[^\\-]+\\/search_network\\-p(\\d+).htm");

	Pattern routeCodePattern = Pattern.compile("/corp-([^\\-]+)\\/route-([^\\.]+)\\.htm");

	Pattern networkCodePattern = Pattern.compile("/corp-([^\\-]+)\\/network-([^\\.]+)\\.htm");

	public Ali56ProcessTask(TaskPool pool, Page page) {
		super(pool, page);
	}

	public List<StoreTask> process() {
		System.out.println(Thread.currentThread().getId() + ": Processing " + page.getUrl());
		String content = fetch("gbk");
		List<StoreTask> result = new Vector<StoreTask>();
		List<StoreTask> stasks = buildEntities(content, page.getUrl());
		result.addAll(stasks);

		Matcher listMatcher = routesPattern.matcher(content);
		while (listMatcher.find()) {
			String listURL = Ali56Crawler.ALI56DOMAIN + StringEscapeUtils.unescapeHtml(listMatcher.group(0))
					+ "?useDefault=1";

			Page listPage = new Page(listURL, Page.STATUS_WAITING, 1);
			pool.addTask(listPage);
		}
		listMatcher = networksPattern.matcher(content);
		while (listMatcher.find()) {
			String listURL = Ali56Crawler.ALI56DOMAIN + StringEscapeUtils.unescapeHtml(listMatcher.group(0))
					+ "?useDefault=1";

			Page listPage = new Page(listURL, Page.STATUS_WAITING, 1);
			pool.addTask(listPage);
		}
		return result;
	}

	private List<StoreTask> buildEntities(String content, String url) {
		List<StoreTask> result = new ArrayList<StoreTask>();
		Matcher matcher = routeCodePattern.matcher(content);

		while (matcher.find()) {
			String word  = StringUtils.trimAllWhitespace(matcher.group(1));
			String routeCode = StringUtils.trimAllWhitespace(matcher.group(2));
			Ali56Route route = new Ali56Route(word, routeCode, Ali56Crawler.ALI56DOMAIN + matcher.group(0) );
			StoreTask stask = pool.createStoreTask(route);
			result.add(stask);
		}

		matcher = networkCodePattern.matcher(content);
		while (matcher.find()) {
			String word  = StringUtils.trimAllWhitespace(matcher.group(1));
			String networkCode = StringUtils.trimAllWhitespace(matcher.group(2));
			Ali56Network route = new Ali56Network(word, networkCode, Ali56Crawler.ALI56DOMAIN + matcher.group(0) );
			StoreTask stask = pool.createStoreTask(route);
			result.add(stask);
		}

		return result;
	}

}
