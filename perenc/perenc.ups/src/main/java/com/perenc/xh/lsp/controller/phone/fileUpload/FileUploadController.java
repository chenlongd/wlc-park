package com.perenc.xh.lsp.controller.phone.fileUpload;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.PhoneReturnJson;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.utils.aliyunoss.AliyunUploadImage;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("api")
public class FileUploadController {

	Logger logger = Logger.getLogger(FileUploadController.class);


	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public ReturnJsonData headImgUpload(HttpServletRequest request, HttpServletResponse response)throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/json; charset=utf-8");
		int type = ServletRequestUtils.getIntParameter(request, "type", 1);
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("file");
		Map<String, Object> value = new HashMap<String, Object>();
		value.put("success", true);
		value.put("errorCode", 0);
		value.put("errorMsg", "");
		try {
            String head = "";
            if(1 == type){
                if (file.getSize() > 1024 * 1024 * 5) {
					return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "上传图片大小不能超过5M！", null);
                }
               head = updateHead(file);
            }else if(2 == type){
				System.out.println("视频大小"+file.getSize());
				if (file.getSize() > 1024 * 1024 * 50) {
					return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "上传视频大小不能超过50M！", null);
                }
                head = updateHead(file);
            }else if(3 == type){
                if (file.getSize() > 1024 * 1024 * 10) {
					return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "上传语音大小不能超过10M！", null);
                }
                head = updateHead(file);
            }
            ReturnJsonData json = new ReturnJsonData();
            Map<String,Object> condition = new HashMap<>();
			condition.put("imgUrl",head);
            json.setCode(0);
            json.setMsg("获取数据成功");
            json.setData(condition);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return new ReturnJsonData(DataCodeUtil.UPLOAD_FILE, "上传失败", null);
        }
	}

	public String updateHead(MultipartFile file) throws IOException{
		if (file == null || file.getSize() <= 0) {
			throw new RuntimeException("文件不能为空");
		}
		AliyunUploadImage aliyunUploadImage = new AliyunUploadImage();
		String name = aliyunUploadImage.uploadImgOss(file);
		String imgUrl = aliyunUploadImage.getImgUrl(name);
		return imgUrl;
	}

	@RequestMapping(value = "/uploadManyFile", method = RequestMethod.POST)
	@ResponseBody
	public ReturnJsonData uploadManyFile(HttpServletRequest request, HttpServletResponse response)throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/json; charset=utf-8");
		int type = ServletRequestUtils.getIntParameter(request, "type", 1);
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<MultipartFile> files = multipartRequest.getFiles("file");
		Map<String, Object> value = new HashMap<String, Object>();
		value.put("success", true);
		value.put("errorCode", 0);
		value.put("errorMsg", "");
		try {
			List<String> imageList = new ArrayList<>();
			String head = "";
			if(type == 1){
				for(MultipartFile file : files) {
					if (file.getSize() > 1024 * 1024 * 5) {
						return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "上传图片大小不能超过5M！", null);
					}
					head = updateHead(file);
					imageList.add(head);
				}
			}else if(type == 2){
				for(MultipartFile file : files) {
					if (file.getSize() > 1024 * 1024 * 50) {
						return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "上传视频大小不能超过50M！", null);
					}
					head = updateHead(file);
					imageList.add(head);
				}
			}else if(type == 3){
				for(MultipartFile file : files) {
					if (file.getSize() > 1024 * 1024 * 10) {
						return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "上传语音大小不能超过10M！", null);
					}
					head = updateHead(file);
					imageList.add(head);
				}
			}

			ReturnJsonData json = new ReturnJsonData();
			Map<String,Object> condition = new HashMap<>();
			condition.put("imageList",imageList);
			json.setCode(0);
			json.setMsg("获取数据成功");
			json.setData(condition);
		} catch (Exception e) {
			e.printStackTrace();
			return new ReturnJsonData(DataCodeUtil.UPLOAD_FILE, "上传失败", null);
		}
		return new ReturnJsonData(DataCodeUtil.UPLOAD_FILE, "上传失败", null);
	}

	@RequestMapping("fileUpload")
	@ResponseBody
	public PhoneReturnJson fileUpload2(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/json; charset=utf-8");

		String certFilePath="E:/"+UUID.randomUUID().toString().replace("-","");
		// linux下
		if ("/".equals(File.separator)) {
			certFilePath = "/usr/local/nginx/html/"+UUID.randomUUID().toString().replace("-","");
		}
		File newFile=new File(certFilePath);
		if (!newFile.isDirectory()) {
			if(newFile.mkdirs()){
				//通过CommonsMultipartFile的方法直接写文件（注意这个时候）
				File createFile=new File(certFilePath+"/"+file.getOriginalFilename());
				file.transferTo(createFile);
				Map<String,Object> map = new HashMap<>();
				map.put("fileUrl",certFilePath+"/"+file.getOriginalFilename());
				return new PhoneReturnJson(true,"上传文件成功",map);
			}
		}else{
			File createFile=new File(certFilePath+"/"+file.getOriginalFilename());
			file.transferTo(createFile);
			Map<String,Object> map = new HashMap<>();
			map.put("fileUrl",certFilePath+"/"+file.getOriginalFilename());
			return new PhoneReturnJson(true,"上传文件成功",map);
		}
		return new PhoneReturnJson(false,"上传失败",null);
	}



}
