package com.ta.internal.multipart.upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * This class uploads an image or video form a path to server
 * 
 * @author vinay
 * 
 */
public class UploaderTask
{
	private static final String TAG = "MultiPartUploaderTask";
	public static String MSG_FILE_NOT_FOUND = "Fielpath is invalid or null, try again.";
	public static String MSG_CONNECTION_ERROR = "Error connecting remote server, try again.";
	/*
	 * private Context mContext;
	 * 
	 * public UploaderTask(Context context) { mContext = context; }
	 */
	private Thread uploadThread;

	/**
	 * uplaod the media file along with other params. other params may be null
	 * in case not required.
	 * 
	 * @param url
	 *            -url of the web interface uploader file on server
	 * @param imageOrimageOrVideoPath
	 *            - absolute path of the image/video file
	 * @param uploadparams
	 *            -additional parameter to be saved with image on server. e.g.
	 *            user_id, username, user_location
	 */
	public void uploadFile(final Handler uploadHandler)
	{
		
		uploadThread = new Thread(new Runnable() {
			public void run()
			{
				String res = "";
				try
				{
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(url);
					
					MultipartEntity reqEntity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE);
					
					if (fileparams != null)
						reqEntity = addFileParams(reqEntity);
					if (uploadparams != null)
						reqEntity = addMoreParams(reqEntity);

					httppost.setEntity(reqEntity);
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity resEntity = response.getEntity();
					
					if (resEntity != null)
					{
						res = EntityUtils.toString(resEntity);
						System.out.println(res);
					} // end if

					if (resEntity != null)
					{
						resEntity.consumeContent();
					} // end if
					httpclient.getConnectionManager().shutdown();
					Message msg = Message.obtain();
					// msg.obj = res;
					Bundle b = new Bundle();
					b.putString("result", res);
//					b.putString("path", getPath());
					b.putString("message", "Upload successful.");

					if (res.contains("true"))
						msg.what = 1;
					else
						msg.what = -1;
					msg.setData(b);
					uploadHandler.sendMessage(msg);
				}
				catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
					Message msg = Message.obtain();
					// msg.obj = res;
					Bundle b = new Bundle();
					b.putString("result", res);
//					b.putString("path", getPath());
					b.putString("message", MSG_CONNECTION_ERROR + "");
					msg.what = -1;
					msg.setData(b);
					uploadHandler.sendMessage(msg);
				}
				catch (ClientProtocolException e)
				{
					e.printStackTrace();
					Message msg = Message.obtain();
					// msg.obj = res;
					Bundle b = new Bundle();
					b.putString("result", res);
//					b.putString("path", getPath());
					b.putString("message", MSG_CONNECTION_ERROR + "");
					msg.what = -1;
					msg.setData(b);
					uploadHandler.sendMessage(msg);
				}
				catch (FileNotFoundException e)
				{
					Message msg = Message.obtain();
					// msg.obj = res;
					Bundle b = new Bundle();
					b.putString("result", "FileNotFoundException");
//					b.putString("path", getPath());
					b.putString("message", MSG_FILE_NOT_FOUND + "");
					msg.what = -1;
					msg.setData(b);
					uploadHandler.sendMessage(msg);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					Message msg = Message.obtain();
					// msg.obj = res;
					Bundle b = new Bundle();
					b.putString("result", "IOException");
//					b.putString("path", getPath());
					b.putString("message", MSG_FILE_NOT_FOUND + "");
					msg.what = -1;
					msg.setData(b);
					uploadHandler.sendMessage(msg);
				}
			}


		});
		uploadThread.start();

	}

	/**
	 * Add additional params to upload request; <br/>
	 * such as user_id, username, reference_id for the user who's uploading this
	 * image.
	 * 
	 * @param reqEntity
	 * @param moreparams
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private MultipartEntity addMoreParams(MultipartEntity reqEntity
			) throws UnsupportedEncodingException
	{
		for (int i = 0; i < uploadparams.size(); i++)
		{
			reqEntity.addPart(uploadparams.get(i).getParamKey(), new StringBody(
					uploadparams.get(i).getParamValue()));
		}

		return reqEntity;
	}
	/**
	 * Add file params to the request entity
	 * @param reqEntity
	 * @param fileparams
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private MultipartEntity addFileParams(MultipartEntity reqEntity) throws UnsupportedEncodingException
	{
		
		for (UploadParams file:fileparams)
		{
			Log.d(TAG + " "+file.getParamKey(), file.getParamValue());
			
			FileBody filebodyVideo = new FileBody(new File(file.getParamValue()));
			
			reqEntity.addPart(file.getParamKey(),filebodyVideo);
			
		}

		return reqEntity;
	}

	private String url;

	private List<UploadParams> uploadparams;
	private List<UploadParams> fileparams;
	public void setUploadParams(List<UploadParams> fileparams,List<UploadParams> moreparams)
	{
		this.uploadparams = moreparams;
		this.fileparams = fileparams;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}


}
