package com.example.lenovo.maandroid;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;

import com.example.lenovo.maandroid.Community.Comment;
import com.example.lenovo.maandroid.Community.Parent;
import com.example.lenovo.maandroid.Community.Post;
import com.example.lenovo.maandroid.Community.PostAdapter;
import com.example.lenovo.maandroid.Community.PostImg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {
    private List<Post> posts = new ArrayList<>();
    private ListView listView;
    private PostAdapter postAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newView = inflater.inflate(R.layout.community_main, container, false);
        listView = newView.findViewById(R.id.listView_community);

        PostTask task = new PostTask();
        task.execute();

        return newView;

    }

    private class PostTask extends AsyncTask {
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            postAdapter = new PostAdapter(posts, getActivity(), R.layout.community_item);
            listView.setAdapter(postAdapter);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.e("post", "开始");
            try {
                URL url = new URL("http://10.7.88.125:8080/Java/PostListServlet");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                JSONObject User_id = new JSONObject();

                User_id.put("praiserId",1);//发送登录者ID

                OutputStream os = con.getOutputStream();
                os.write(User_id.toString().getBytes());

                InputStream is = con.getInputStream();
                byte[] bs = new byte[255];
                int len;
                StringBuffer sb = new StringBuffer();
                while ((len = is.read(bs)) != -1) {
                    sb.append(new String(bs, 0, len));
                }
                String get = new String(sb);
                JSONArray getArray = new JSONArray(get);

                for (int i = 0; i < getArray.length(); i++) {
                    JSONObject object = getArray.getJSONObject(i);
                    Post post = new Post();
                    post.setId(object.getInt("id"));
                    post.setContent(object.getString("content"));
                    post.setTime(Timestamp.valueOf(object.getString("time")));
                    post.setPraiseCount(object.getInt("praiseCount"));
                    //发帖人
                    Parent poster = new Parent();
                    poster.setHeaderPath(object.getString("headerPath"));
                    poster.setNickName(object.getString("nickName"));
                    post.setParent(poster);
                    //imgs
                    JSONArray imgs = object.getJSONArray("imgs");

                    List<PostImg> imges = new ArrayList<>();
                    for (int j = 0; j < imgs.length(); j++) {
                            JSONObject img = imgs.getJSONObject(j);
                            PostImg image = new PostImg();
                            image.setPostId(img.optInt("postId"));
                            image.setTime(Timestamp.valueOf(img.optString("Pimg_time")));
                            image.setId(img.optInt("Pimg_id"));
                            image.setPath(img.optString("path"));
                        imges.add(image);
                    }
                    post.setImgs(imges);
                    //3_comment
                    JSONArray comment_3 = object.getJSONArray("comments");
                    List<Comment> comments = new ArrayList<>();
                    for (int k = 0; k < comment_3.length(); k++) {
                        JSONObject comment = comment_3.getJSONObject(k);
                        Comment comment1 = new Comment();
                        comment1.setContent(comment.optString("commentContent"));
                        Parent commentator = new Parent();
                        commentator.setNickName(comment.optString("commentatorName"));
                        comment1.setCommentator(commentator);
                        comments.add(comment1);
                    }
                    post.setComments(comments);
                    //isPraise
                    post.setIsPraise(object.getInt("isPraise"));
                    Log.e("isPraise",object.getInt("isPraise")+"");
                    posts.add(post);
                }

                is.close();
                os.close();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return posts;
        }
    }


}
