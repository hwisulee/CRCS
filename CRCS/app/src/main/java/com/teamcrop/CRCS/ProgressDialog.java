package com.teamcrop.CRCS;

import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;

public class ProgressDialog extends Dialog {
    public ProgressDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_progress);
        setCancelable(false);

        ProgressBar progressBar = findViewById(R.id.spin_kit);
        Sprite faddingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(faddingCircle);
    }
}
