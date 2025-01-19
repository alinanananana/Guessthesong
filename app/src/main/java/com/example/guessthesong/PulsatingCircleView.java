package com.example.guessthesong;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import android.animation.ValueAnimator;

public class PulsatingCircleView extends View {

    private Paint paintMainCircle; // Краска для основного круга
    private Paint paintOuterCircle1; // Краска для первого внешнего круга
    private Paint paintOuterCircle2; // Краска для второго внешнего круга

    private float radius; // Текущий радиус основного круга
    private float maxRadius; // Максимальный радиус основного круга
    private float minRadius; // Минимальный радиус основного круга

    public PulsatingCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PulsatingCircleView(Context context) {
        super(context);
        init();
    }

    private void init() {
        // Настройка краски для основного круга
        paintMainCircle = new Paint();
        paintMainCircle.setColor(0xFF6200EE); // Основной цвет (фиолетовый)
        paintMainCircle.setStyle(Paint.Style.FILL);
        paintMainCircle.setAntiAlias(true);

        // Настройка краски для первого внешнего круга (полупрозрачный)
        paintOuterCircle1 = new Paint();
        paintOuterCircle1.setColor(0x556200EE); // Полупрозрачный фиолетовый
        paintOuterCircle1.setStyle(Paint.Style.FILL);
        paintOuterCircle1.setAntiAlias(true);

        // Настройка краски для второго внешнего круга (еще более прозрачный)
        paintOuterCircle2 = new Paint();
        paintOuterCircle2.setColor(0x336200EE); // Прозрачный фиолетовый
        paintOuterCircle2.setStyle(Paint.Style.FILL);
        paintOuterCircle2.setAntiAlias(true);

        // Параметры радиуса
        minRadius = 50f; // Минимальный радиус основного круга
        maxRadius = 150f; // Максимальный радиус основного круга
        radius = minRadius;

        // Запуск анимации пульсации
        startPulsatingAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Центр круга
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;

        // Рисуем внешний второй круг (самый большой и самый прозрачный)
        canvas.drawCircle(cx, cy, radius + 60f, paintOuterCircle2);

        // Рисуем внешний первый круг (средний радиус и прозрачность)
        canvas.drawCircle(cx, cy, radius + 30f, paintOuterCircle1);

        // Рисуем основной круг
        canvas.drawCircle(cx, cy, radius, paintMainCircle);
    }

    private void startPulsatingAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(minRadius, maxRadius);
        animator.setDuration(1000); // Длительность одного цикла (1 секунда)
        animator.setRepeatMode(ValueAnimator.REVERSE); // Возврат к минимальному радиусу
        animator.setRepeatCount(ValueAnimator.INFINITE); // Бесконечная анимация

        animator.addUpdateListener(animation -> {
            radius = (float) animation.getAnimatedValue(); // Изменяем радиус
            invalidate(); // Перерисовываем View
        });

        animator.start();
    }
}