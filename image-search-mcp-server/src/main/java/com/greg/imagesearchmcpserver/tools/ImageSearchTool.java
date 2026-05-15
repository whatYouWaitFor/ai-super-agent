package com.greg.imagesearchmcpserver.tools;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
public class ImageSearchTool {

    private static final String API_KEY = "你的api-key";

    @McpTool(description = "搜索Pexels图片，返回medium尺寸的图片URL列表")
    public String searchPhotos(@McpToolParam(description = "图片查询关键词") String query) {
        String url = "https://api.pexels.com/v1/search?query=" + query + "&per_page=8";
        //String apiKey = "YOUR_API_KEY";  // 建议从配置读取

        String responseBody = HttpRequest.get(url)
                .header("Authorization", API_KEY)
                .execute()
                .body();

        JSONObject json = JSONUtil.parseObj(responseBody);
        JSONArray photos = json.getJSONArray("photos");

        // 只获取中等尺寸的图片URL
        List<String> mediumUrls = new ArrayList<>();
        for (int i = 0; i < photos.size(); i++) {
            String mediumUrl = photos.getJSONObject(i)
                    .getJSONObject("src")
                    .getStr("medium");
            mediumUrls.add(mediumUrl);
        }
        // 转换成，逗号分隔的图片URL字符串
        String mediumUrlsStr = String.join(",", mediumUrls);
        return mediumUrlsStr;
    }
}
