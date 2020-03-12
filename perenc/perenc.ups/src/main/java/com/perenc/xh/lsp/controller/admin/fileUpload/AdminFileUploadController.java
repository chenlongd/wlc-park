package com.perenc.xh.lsp.controller.admin.fileUpload;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.Json;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.utils.aliyunoss.AliyunUploadImage;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("upload")
public class AdminFileUploadController {

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public ReturnJsonData uploadFile(HttpServletRequest request, HttpServletResponse response)throws Exception {
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
                    throw new RuntimeException("上传图片大小不能超过5M！");
                }
               head = updateHead(file);
            }else if(2 == type){
				System.out.println("视频大小"+file.getSize());
				if (file.getSize() > 1024 * 1024 * 50) {
                    throw new RuntimeException("上传视频大小不能超过50M！");
                }
                head = updateHead(file);
            }else if(3 == type){
                if (file.getSize() > 1024 * 1024 * 10) {
                    throw new RuntimeException("上传语音大小不能超过10M！");
                }
                head = updateHead(file);
            }
			ReturnJsonData json = new ReturnJsonData();
			Map<String,Object> condition = new HashMap<>();
			condition.put("imgUrl",head);
			json.setCode(0);
			json.setMsg("上传文件成功");
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
	public Json uploadManyFile(HttpServletRequest request, HttpServletResponse response)throws Exception {
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
						throw new RuntimeException("上传图片大小不能超过5M！");
					}
					head = updateHead(file);
					imageList.add(head);
				}
			}else if(type == 2){
				for(MultipartFile file : files) {
					if (file.getSize() > 1024 * 1024 * 50) {
						throw new RuntimeException("上传视频大小不能超过50M！");
					}
					head = updateHead(file);
					imageList.add(head);
				}
			}else if(type == 3){
				for(MultipartFile file : files) {
					if (file.getSize() > 1024 * 1024 * 10) {
						throw new RuntimeException("上传语音大小不能超过10M！");
					}
					head = updateHead(file);
					imageList.add(head);
				}
			}
			Map<String,Object> param = new HashMap<>();
			param.put("imageList",imageList);
			return new Json(true,"获取数据成功",param);
		} catch (Exception e) {
			return new Json(false, e.getMessage(), null);
		}
	}

	@RequestMapping("fileUpload")
	@ResponseBody
	public ReturnJsonData fileUpload(@RequestParam("file") CommonsMultipartFile file,HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
		String appId = ServletRequestUtils.getStringParameter(request,"appId","");
		if(StringUtils.isNotEmpty(appId)){
			String certFilePath="E:/"+appId;
			// linux下
			if ("/".equals(File.separator)) {
				certFilePath = "/usr/local/nginx/html/"+appId;
			}
			File newFile=new File(certFilePath);
			if (!newFile.isDirectory()) {
				if(newFile.mkdirs()){
					//通过CommonsMultipartFile的方法直接写文件（注意这个时候）
					File createFile=new File(certFilePath+"/"+file.getOriginalFilename());
					file.transferTo(createFile);
					Map<String,Object> map = new HashMap<>();
					map.put("fileUrl",certFilePath+"/"+file.getOriginalFilename());
					return new ReturnJsonData(DataCodeUtil.SUCCESS,"上传文件成功",map);
				}
			}else{
				File createFile=new File(certFilePath+"/"+file.getOriginalFilename());
				file.transferTo(createFile);
				Map<String,Object> map = new HashMap<>();
				map.put("fileUrl",certFilePath+"/"+file.getOriginalFilename());
				return new ReturnJsonData(DataCodeUtil.SUCCESS,"上传文件成功",map);
			}
		}else{
			return new ReturnJsonData(DataCodeUtil.UPLOAD_FILE,"请先完善公众号信息",null);
		}
		return new ReturnJsonData(DataCodeUtil.UPLOAD_FILE,"上传文件失败",null);
	}


}
