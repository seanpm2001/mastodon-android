package org.joinmastodon.android.api.requests.statuses;

import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import org.joinmastodon.android.MastodonApp;
import org.joinmastodon.android.api.ContentUriRequestBody;
import org.joinmastodon.android.api.MastodonAPIRequest;
import org.joinmastodon.android.api.ProgressListener;
import org.joinmastodon.android.api.ResizedImageRequestBody;
import org.joinmastodon.android.model.Attachment;
import org.joinmastodon.android.ui.utils.UiUtils;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadAttachment extends MastodonAPIRequest<Attachment>{
	private Uri uri;
	private ProgressListener progressListener;
	private int maxImageSize;

	public UploadAttachment(Uri uri){
		super(HttpMethod.POST, "/media", Attachment.class);
		this.uri=uri;
	}

	public UploadAttachment(Uri uri, int maxImageSize){
		this(uri);
		this.maxImageSize=maxImageSize;
	}

	public UploadAttachment setProgressListener(ProgressListener progressListener){
		this.progressListener=progressListener;
		return this;
	}

	@Override
	public RequestBody getRequestBody() throws IOException{
		return new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("file", UiUtils.getFileName(uri), maxImageSize>0 ? new ResizedImageRequestBody(uri, maxImageSize, progressListener) : new ContentUriRequestBody(uri, progressListener))
				.build();
	}
}
