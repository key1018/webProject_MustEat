package com.mz.mybatis.store.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.mz.mybatis.common.MyFileRenamePolicy;
import com.mz.mybatis.store.model.service.StoreService;
import com.mz.mybatis.store.model.vo.Store;
import com.oreilly.servlet.MultipartRequest;

/**
 * Servlet implementation class StoreInsertController
 */
@WebServlet("/storeinsert.st")
public class StoreInsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StoreInsertController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
request.setCharacterEncoding("UTF-8");
		
		HttpSession session = request.getSession();
		
		if(ServletFileUpload.isMultipartContent(request)) {
			
		String SavePath = session.getServletContext().getRealPath("/resources/image/jsw/store_upfiles/");
		
		int maxSize = 10 * 1024 * 1024;
		
		MultipartRequest multiRequest = new MultipartRequest(request, SavePath, maxSize, "UTF-8", new MyFileRenamePolicy());
		
		String storeName = multiRequest.getParameter("storename");
		String localSi = multiRequest.getParameter("sido");
		String localGu = multiRequest.getParameter("gugun");
		String localRo = multiRequest.getParameter("storeaddress");
		String[] storeTagArr = multiRequest.getParameterValues("storemood");
		String storePhone = multiRequest.getParameter("storephone");
		String storeAddress = multiRequest.getParameter("storefulladdress");		
		String storeIntro = multiRequest.getParameter("storeintro");
		String storePopularity = multiRequest.getParameter("storemenu");
		String storePopInfo = multiRequest.getParameter("storemenuintro");
		String storeOperating = multiRequest.getParameter("storeoperating1") + "~" + multiRequest.getParameter("storeoperating2");
		String storeBreaktime = multiRequest.getParameter("storebreak1") + "~" + multiRequest.getParameter("storebreak2");
		String naverAddress = multiRequest.getParameter("storenaverurl");
		String dayOff = multiRequest.getParameter("storeholiday");
		String storeUrl = multiRequest.getParameter("storeurl");
		
		String storeTag = "";
		if(storeTagArr != null) {
			storeTag = String.join(",", storeTagArr);
		}
		
		Store se = new Store();		
		se.setStoreName(storeName);
		se.setLocalSi(localSi);
		se.setLocalGu(localGu);
		se.setLocalRo(localRo);
		se.setStoreTag(storeTag);
		se.setStoreAddress(storeAddress);
		se.setStorePhone(storePhone);
		se.setStoreIntro(storeIntro);
		se.setStorePopularity(storePopularity);
		se.setStorePopInfo(storePopInfo);
		se.setStoreOperating(storeOperating);
		se.setStoreBreaktime(storeBreaktime);
		se.setNaverAddress(naverAddress);
		se.setDayOff(dayOff);
		se.setStoreUrl(storeUrl);
		
		if(multiRequest.getOriginalFileName("storeimg") != null) {
			String storeImgPath = "resources/image/jsw/store_upfiles/" + multiRequest.getFilesystemName("storeimg");
			se.setStoreImgPath(storeImgPath);
		}
		
		if(multiRequest.getOriginalFileName("storemenuimg") != null) {
			String storePopPath = "resources/image/jsw/store_upfiles/" + multiRequest.getFilesystemName("storemenuimg");
			se.setStorePopPath(storePopPath);
		}
				

		int result = new StoreService().storelistInsert(se);
				
		if(result > 0) {
			// 등록 성공
			session.setAttribute("alertMsg", "성공적으로 식당 등록이 완료되었습니다.");
			response.sendRedirect(request.getContextPath() + "/storeadminlist.st");
		}else { 
			// 등록 실패
			
			if(multiRequest.getOriginalFileName("storeimg") != null) {
				new File(SavePath + multiRequest.getFilesystemName("storeimg")).delete();
			}
			
			if(multiRequest.getOriginalFileName("storemenuimg") != null) {
				new File(SavePath + multiRequest.getFilesystemName("storemenuimg")).delete();
			}
			
			request.setAttribute("alertMsg", "식당 등록 실패");
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
		}
		
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
