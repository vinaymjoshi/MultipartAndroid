package com.ta.internal.multipart.upload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Sample class to demonstrate the api uses
 * @author vinay
 *
 */
public class MultiPartSampleActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		
//		UploaderTask.MSG_CONNECTION_ERROR="Set your custome message here";
//		UploaderTask.MSG_FILE_NOT_FOUND="set your custome messge here for file not found";
		
		String url="url here";
		String pathParamKey="video_path";
		String filePath="path of video or image";//Environment.getExternalStorageDirectory()+"/11.png";
		
		List<UploadParams> fileparams=new ArrayList<UploadParams>();
		//file 1 (img or video)
		fileparams.add(addParam("img1","sdcard/path/img1.png"));
		//file 2(img or video)
		fileparams.add(addParam("img2","sdcard/path/img1.png"));
		//file 3(img or video)
		fileparams.add(addParam("video1","sdcard/path/video1.png"));
		
		//add additional params one by one
		List<UploadParams> moreparams=new ArrayList<UploadParams>();
		
		//example param 1
		moreparams.add(addParam("provider_id","123"));
		//example param 2
		moreparams.add(addParam("attandent_id","10"));
		//add as many params you need
		
		TaMultiPartUploader tamultipartuploader = new TaMultiPartUploader(url,fileparams,moreparams) {
			
			@Override
			public void onUploadFaild(/*String path,*/ String message)
			{
				// TODO Auto-generated method stub
				Log.e("Message:",message);
				showMessageDialog(MultiPartSampleActivity.this, message);
			}
			
			@Override
			public void onSuccessUpload(/*String path,*/ String message)
			{
				// TODO Auto-generated method stub
				//1. you may want to delete the image after upload
				//show alert or toast if required
				Log.e("Message:",message);
				showMessageDialog(MultiPartSampleActivity.this, message);
			}
		};
		tamultipartuploader.exequte();
	}

	void showMessageDialog(final Context context, String message)
	{
		if (context != null)
		{
			String title = "Message";
			new AlertDialog.Builder(context).setTitle(title)
			.setMessage(message)
			.setCancelable(false).setPositiveButton(context.getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int whichButton)
				{
					dialog.dismiss();
				}
			}).create().show();
		}
	}
	
	/**
	 * add a new key value pair 
	 * @param string
	 * @param string2
	 * @return 
	 */
	public UploadParams addParam(String key, String value)
	{
		UploadParams param = new UploadParams();
		param.setParamKey(key);
		param.setParamValue(value);
		return param;
	}

}
