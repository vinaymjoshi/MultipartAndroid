package com.ta.internal.multipart.upload;

import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * This class is a reusable unit under TechAhead re-usability projects. 
 * @author vinay mohan joshi
 *
 */
public abstract class TaMultiPartUploader
{
	protected static final String TAG = "TaMultiPartUploader";
	private UploaderTask uploader;
	
	/**
	 * method to call on upload succeed.
	 * @param path - path of the media uplaoded. you may want to delete the file after upload.
	 * @param message - message to show the user. "upload successful."
	 */
	public abstract void onSuccessUpload(/*String path,*/ String message);
	/**
	 * method to call on upload failed.
	 * @param path - path of the media uplaoded. you may want to delete the file after upload.
	 * @param message - message to show the user. "upload failed."
	 */
	public abstract void onUploadFaild(/*String path,*/ String message);
	
	/**
	 * initialize uploader controller here.
	 * @param url 
	 *            -url of the web interface uploader file on server 
	 * @param path - absolute path form sd card of the image/video file
	 * @param moreparams -additional parameter to be saved with image on server. e.g.
	 *            user_id, username, user_location
	 */
	public TaMultiPartUploader(/*Context context, */String url,List<UploadParams> fileparams,List<UploadParams> params)
	{
		uploader = new UploaderTask();
		uploader.setUrl(url);
//		uploader.setPath(path);
//		uploader.setFilePathParamKey(filePathParamKey);
		
		uploader.setUploadParams(fileparams, params);
	}
	public void exequte()
	{
		uploader.uploadFile(responseHandler);
	}
	
	private Handler responseHandler=new Handler()
	{
		@Override public void handleMessage(Message msg)
		{
			//String path =msg.getData().getString("path"); 
			String response=msg.getData().getString("result").toString(); 
			Log.d(TAG,(String)response);
			switch (msg.what)
			{
			case -1:
				onUploadFaild(response);
				break;
			case 1:
				onSuccessUpload(response);
				break;
			default:
				onUploadFaild(response);
				break;
			}
		}
	};
}
