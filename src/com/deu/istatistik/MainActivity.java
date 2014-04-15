package com.deu.istatistik;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.deu.istatistik.R;

public class MainActivity extends Activity implements AnimationListener {

	Animation animasyon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acilis);

		animasyon = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.deulogoanim);
		animasyon.setAnimationListener(this);

		
		ImageView logo = (ImageView) findViewById(R.id.deulogo);
		logo.startAnimation(animasyon);
		
		Thread kanal = new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();

				try {

					sleep(0);
					Intent intent = new Intent("com.deu.istatistik.ACILIS");
					startActivity(intent);

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					finish();
				}
			}

		};

		kanal.start();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

}
