package com.gridone.scraping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gridone.scraping.component.WebScraping;
import com.gridone.scraping.service.KeywordService;
import com.gridone.scraping.service.NewsService;

@Controller
public class TestController {

	@Autowired
	WebScraping webScraping;
	
	@Autowired
	KeywordService keywordService;
	
	@Autowired
	NewsService newsService;
	
	@Autowired
//	DepartmentMapper departmentMapper;
	
//	@RequestMapping("/testtest1")
//	public @ResponseBody Object test() {
//		List<DepartmentModel> data = departmentMapper.getDepart();
//		
//		List<DepartTree> tree = new ArrayList<DepartTree>();
//		
//		
//		Integer lastLevel = data.get(0).getLvl();
//		for(int i = 0; i < data.size(); i++) {
//			
//			if(i == 0) { // last child
//				DepartTree last = new DepartTree();
//				last.setId(data.get(i).getId());
//				last.setParentId(data.get(i).getParentId());
//				last.setName(data.get(i).getName());
//				last.setLvl(data.get(i).getLvl());
//
//				tree.add(last);
//			}else {
//				DepartTree last = new DepartTree();
//				last.setId(data.get(i).getId());
//				last.setParentId(data.get(i).getParentId());
//				last.setName(data.get(i).getName());
//				last.setLvl(data.get(i).getLvl());
//
//				List<DepartTree> removeTarget = new ArrayList<>();
//				
//				for (DepartTree t : tree) {
//					System.out.println("t : "+t);
//					if(data.get(i).getId() == t.getParentId()) {
//						if(t.getNodes().size() == 0) {							
//							t.setNodes(null);
//						}
//						last.addNodes(t);
//						removeTarget.add(t);
//					}
//					
//				}
//				tree.add(last);
//				
//				for (DepartTree r : removeTarget) {
//					tree.remove(r);
//				}
//				
//				
//				
//			}
//			
//		}
//		
//		System.out.println("tree : "+tree);
//		
//		return tree;
//	}
	
	@RequestMapping("/test2")
	public String test2() {
		/*List<Keyword> list = keywordService.selectAll();
		System.err.println("list : "+list);
		for (Keyword k : list) {
			String keywords = k.getEnterprise() + " \"" + k.getKeywords().split(",")[0] + "\"" + " \"" + k.getKeywords().split(",")[1]+ "\"";
//			System.err.println("keywords : "+keywords);
		}
		Keyword uiPath = list.get(list.size()-1);
		String keywords = uiPath.getEnterprise() + "%20\"" + uiPath.getKeywords().split(",")[0] + "\"" + "%20\"" + uiPath.getKeywords().split(",")[1]+ "\"";
		System.err.println("uiPath : "+uiPath);
		System.err.println("keywords : "+keywords);
		try {
			newsService.getNewsScraping(keywords, uiPath);
			Thread.sleep((int)(Math.random()*3000)+500);
		} catch (Exception e) {
			e.printStackTrace();
		}	*/	
		
		return "dailyNews";
	}
	
	
	
	
	
}
