package com.chepizhko.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

public class SunsetFragment extends Fragment {
    private View mSceneView;
    private View mSunView;
    private View mSkyView;
    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    public static SunsetFragment newInstance() {
        return new SunsetFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sunset, container, false);
        mSceneView = view;
        mSunView = view.findViewById(R.id.sun);
        mSkyView = view.findViewById(R.id.sky);
        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);
        // подключите метод startAnimation(), чтобы он выполнялся каждый раз, когда пользователь касается любой точки сцены
        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });
        return view;
    }
    // следует определить начальное и конечное состояния анимации. Этот первый шаг будет реализован в этом методе
    private void startAnimation() {
        // анимация будет начинаться от верха текущего положения представления и заканчиваться в состоянии,
        // при котором верх находится у нижнего края родителя mSunView
        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getHeight();
        // создайте и запустите экземпляр ObjectAnimator для выполнения анимации
        // ObjectAnimator называется аниматором свойства. Ничего не зная о том, как перемещать представление по экрану,
        // аниматор свойства многократно вызывает set-методы свойства с разными значениями
        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", sunYStart, sunYEnd)
                // время между прорисовками
                .setDuration(3000);
        // дополнительная анимацию цвета неба от mBlueSkyColor до mSunsetSkyColor
        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor)
                .setDuration(3000);
        // небольшое ускорение солнца в начале анимации с использованием объекта AccelerateInterpolator
        heightAnimator.setInterpolator(new AccelerateInterpolator());
        // Когда обычного умения ObjectAnimator по вычислению промежуточных значений между начальной и конечной точками
        // оказывается недостаточно,вы можете определить субкласс TypeEvaluator с именем ArgbEvaluator
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());
        // анимация ночного неба
        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());
//        heightAnimator.start();
//        sunsetSkyAnimator.start();

        // Объект AnimatorSet представляет набор анимаций, которые могут воспроизводиться совместно
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(heightAnimator)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator);
        animatorSet.start();
    }
}
