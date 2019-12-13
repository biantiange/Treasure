package com.example.lenovo.maandroid.Record;

import java.util.Arrays;

public class LookByTagEntity {
        //路径
        private String[] imgPaths;
        private String content;  //内容
        private int id;
        private String upTime;

        public String[] getImgPaths() {
            return imgPaths;
        }

        public void setImgPaths(String[] imgPaths) {
            this.imgPaths = imgPaths;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUpTime() {
            return upTime;
        }

        public void setUpTime(String upTime) {
            this.upTime = upTime;
        }

        public LookByTagEntity(String[] imgPaths, String content, int id, String upTime) {
            this.imgPaths = imgPaths;
            this.content = content;
            this.id = id;
            this.upTime = upTime;
        }
        public LookByTagEntity(){

        }

        @Override
        public String toString() {
            return "LookByTagEntity{" +
                    "imgPaths=" + Arrays.toString(imgPaths) +
                    ", content='" + content + '\'' +
                    ", id=" + id +
                    ", upTime='" + upTime + '\'' +
                    '}';
        }


}
