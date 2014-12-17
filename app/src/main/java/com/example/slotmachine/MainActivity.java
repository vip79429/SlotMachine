package com.example.slotmachine;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import com.wheel.widget.OnWheelChangedListener;
import com.wheel.widget.OnWheelScrollListener;
import com.wheel.widget.WheelView;
import com.wheel.widget.adapter.AbstractWheelAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ClickableViewAccessibility")
public class MainActivity extends Activity {
	private ImageView iv_pa;
	private int pre_y;
	private ArrayList<Integer> imageItem; // �x�s���Q�Ϥ����Ҧ��Ϥ�
	private TextView text;
	private Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.slot_machine_layout);
		imageItem = new ArrayList<Integer>();
		text = (TextView) findViewById(R.id.pwd_status);
		mHandler = new Handler();

		initWheel(R.id.slot_1);
		initWheel(R.id.slot_2);
		initWheel_3(R.id.slot_3);

		iv_pa = (ImageView) findViewById(R.id.iv_pa);
		iv_pa.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {// ��touch�ƥ󤣦P�@���P���Ʊ�
				case MotionEvent.ACTION_DOWN:
					// ���U�ɡA���o�ثe��y
					pre_y = (int) event.getY();
					break;
				case MotionEvent.ACTION_MOVE:

					// �P�_move����V
					int now_y = (int) event.getY();
					if (pre_y < now_y) {// ���U
						text.setText("�n�n�n�n�n..");
						iv_pa.setImageResource(R.drawable.down_pa);
						iv_pa.setEnabled(false);
						mixWheel(R.id.slot_1);
						mHandler.postDelayed(slot_2, 500);
						mHandler.postDelayed(slot_3, 1200);

					}

					break;
				}
				return true;
			}
		});
	}

	final Runnable slot_2 = new Runnable() {
		public void run() {
			// TODO Auto-generated method stub
			// �ݭn�I���@����
			mixWheel(R.id.slot_2);
		}
	};

	final Runnable slot_3 = new Runnable() {
		public void run() {
			// TODO Auto-generated method stub
			// �ݭn�I���@����
			mixWheel(R.id.slot_3);
		}
	};
	// Wheel scrolled flag
	private boolean wheelScrolled = false;

	// Wheel scrolled listener
	OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
			wheelScrolled = true;
		}

		public void onScrollingFinished(WheelView wheel) {
			wheelScrolled = false;
			updateStatus();
		}
	};

	// Wheel changed listener
	private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			if (!wheelScrolled) {
//				updateStatus();
			}
		}
	};

	/**
	 * Updates status
	 */
	private void updateStatus() {

		if (test())
			text.setText("���߳s�u!");
		else
			text.setText("�ЦA���A�F..");

		iv_pa.setImageResource(R.drawable.up_pa);
		iv_pa.setEnabled(true);
	}

	/**
	 * Initializes wheel
	 * 
	 * @param id
	 *            the wheel widget Id
	 */
	private void initWheel(int id) {
		WheelView wheel = getWheel(id);
		wheel.setViewAdapter(new SlotMachineAdapter(this));
		wheel.setCurrentItem((int) (Math.random() * 10));
//		wheel.addChangingListener(changedListener);
//		wheel.addScrollingListener(scrolledListener);
		wheel.setCyclic(true);
		wheel.setEnabled(false);
	}

	private void initWheel_3(int id) {
		WheelView wheel = getWheel(id);
		wheel.setViewAdapter(new SlotMachineAdapter(this));
		wheel.setCurrentItem((int) (Math.random() * 10));
		wheel.addScrollingListener(scrolledListener);
		wheel.setCyclic(true);
		wheel.setEnabled(false);
	}
	/**
	 * Returns wheel by Id
	 * 
	 * @param id
	 *            the wheel Id
	 * @return the wheel with passed Id
	 */
	private WheelView getWheel(int id) {
		return (WheelView) findViewById(id);
	}

	/**
	 * Tests wheels
	 * 
	 * @return true
	 */
	private boolean test() {
		// int value = getWheel(R.id.slot_1).getCurrentItem();
		// return testWheelValue(R.id.slot_2, value)
		// && testWheelValue(R.id.slot_3, value);
		String value1 = String.valueOf(imageItem.get(getWheel(R.id.slot_1)
				.getCurrentItem()));// ���o�Ĥ@�����Q��
		String value2 = String.valueOf(imageItem.get(getWheel(R.id.slot_2)
				.getCurrentItem() + imageItem.size() * 1 / 3)); // ���o�ĤG�����Q��
		String value3 = String.valueOf(imageItem.get(getWheel(R.id.slot_3)
				.getCurrentItem() + imageItem.size() * 2 / 3)); // ���o�ĤT�����Q��

		if (value1.equals(value2) && value2.equals(value3)) {
			Log.e("value1", value1);
			Log.e("value2", value2);
			Log.e("value3", value3);
			Log.e("true", "true");
			return true;
		} else {
			Log.e("value1", value1);
			Log.e("value2", value2);
			Log.e("value3", value3);
			Log.e("false", "false");
			return false;
		}

	}

	/**
	 * Tests wheel value
	 * 
	 * @param id
	 *            the wheel Id
	 * @param value
	 *            the value to test
	 * @return true if wheel value is equal to passed value
	 */
	private boolean testWheelValue(int id, int value) {
		return getWheel(id).getCurrentItem() == value;
	}

	/**
	 * Mixes wheel
	 * 
	 * @param id
	 *            the wheel id
	 */
	private void mixWheel(int id) {
		WheelView wheel = getWheel(id);
		wheel.scroll(-350 + (int) (Math.random() * 50), 3000); //����ɶ�
	}

	/**
	 * Slot machine adapter
	 */
	private class SlotMachineAdapter extends AbstractWheelAdapter {
		// Image size
		final int IMAGE_WIDTH = 100; //icon�e��
		final int IMAGE_HEIGHT = 100;

		// Slot machine symbols
		private final int items[] = new int[] { R.drawable.seven,
				R.drawable.money, R.drawable.pineapple, R.drawable.candy };

		// Cached images
		private List<SoftReference<Bitmap>> images;

		// Layout inflater
		private Context context;

		/**
		 * Constructor
		 */
		public SlotMachineAdapter(Context context) {
			this.context = context;

			for (int i = 0; i < 30; i++) {
				int r1, r2;
				do {
					r1 = (int) ((Math.random() * items.length));
					r2 = (int) ((Math.random() * items.length));
				} while (r1 == r2);
				int temp;
				temp = items[r1];
				items[r1] = items[r2];
				items[r2] = temp;
			}

			String bbb = "";
			for (int i = 0; i < items.length; i++) {
				imageItem.add(items[i]);
				bbb = bbb + String.valueOf(items[i] + ",");
			}
			Log.e("bbb", bbb);

			images = new ArrayList<SoftReference<Bitmap>>(items.length);
			for (int id : items) {
				images.add(new SoftReference<Bitmap>(loadImage(id)));
			}
		}

		/**
		 * Loads image from resources
		 */
		private Bitmap loadImage(int id) {
			Bitmap bitmap = BitmapFactory.decodeResource(
					context.getResources(), id);
			Bitmap scaled = Bitmap.createScaledBitmap(bitmap, IMAGE_WIDTH,
					IMAGE_HEIGHT, true);
			bitmap.recycle();
			return scaled;
		}

		@Override
		public int getItemsCount() {
			return items.length;
		}

		// Layout params for image view
		final LayoutParams params = new LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT);

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			ImageView img;
			if (cachedView != null) {
				img = (ImageView) cachedView;
			} else {
				img = new ImageView(context);
			}
			img.setLayoutParams(params);
			SoftReference<Bitmap> bitmapRef = images.get(index);
			Bitmap bitmap = bitmapRef.get();
			if (bitmap == null) {
				bitmap = loadImage(items[index]);
				images.set(index, new SoftReference<Bitmap>(bitmap));
			}
			img.setImageBitmap(bitmap);

			return img;
		}
	}
}
