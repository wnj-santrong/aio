<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<code id="pagename" style="display:none">c:index_a:meeting</code>
            <ul class="meeting">
                <li>
                    <p>视频码率:</p>
                    <span>
                    <select name="rate" class="rate">
                        <option>768kpbs</option>
                        <option>1024kpbs</option>
                        <option>2048kpbs</option>
                        <option>4096kpbs</option>
                    </select>
                    </span> </li>
                <li>
                    <p>视频分辨率:</p>
                    <span>
                    <select name="rate" class="rate">
                        <option>720x480</option>
                        <option>1024x768</option>
                        <option>1280x720</option>
                        <option>1920x1024</option>
                    </select>
                    </span></li>
                
                <li>
                    <p>最大录制时长:</p>
                    <span>
                    <select name="rate" class="rate">
                        <option>1小时20分</option>
                        <option>2小时50分</option>
                        <option>3小时20分</option>
                        <option>8小时30分</option>
                    </select>
                    </span></li>
                <li>
                    <p>文件名:</p>
                    <span>未开启录制</span></li>
                <li>
                    <p>开始时间:</p>
                    <span>未开启录制</span></li>
                <li>
                    <p>数据源地址:</p>
                    <span><input name="text" type="text" value="http://192.168.3.6" class="Data_address"/></span>
                    <a href="#" class="add">增加</a></li>
                <li>
                    <p>数据源状态:</p>
                    <span>正常</span></li>
                <li>
                    <p>录制操作:</p>
                    <span><a href="#">开启录制</a><a href="#">停止录制</a></span></li>
                <li>
                    <p>录制模式:</p>
                    <span>
                    <input name="radio" type="radio" value=""  class="model"/>
                    合成编码录制模式</span><span>
                    <input name="radio" type="radio" value=""  class="model"/>
                    多种合成编码模式</span></li>
                <li>
                    <p>会议管理:</p>
                    <span><a href="#">开启</a><a href="#">结束</a><a href="#">会议设置</a></span></li>
                <li>
                <p>状态:</p>
                    <span>正在开会中......</span>
                </li>
               
            </ul>
            <div class="save "><span><a href="#">保存</a></span></div>


