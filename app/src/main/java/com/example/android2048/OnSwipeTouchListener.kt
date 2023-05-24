package com.example.android2048

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

open class OnSwipeTouchListener(context: Context) : View.OnTouchListener {

    //открытый класс, который реализует интерфейс для обрабокт жество свайпа на view


    private val gestureDetector: GestureDetector //обьект используемый при обноражуние жестов

    companion object {

        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }
    //это константы, которые определяют пороговые значения для определения,
    // считается ли свайп достаточно длинным и с достаточной скоростью соответственно.

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    } //Инициалзация

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        // Метод onTouch обработки события касания на View.
        // Он передает событие gestureDetector
        // для обработки жестов и возвращает результат обработки.
        // Служебный класс интерфейса
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        //GestureListener - это внутренний класс, который расширяет GestureDetector.SimpleOnGestureListener().
        // Он используется для обработки различных жестов.

        override fun onDown(e: MotionEvent): Boolean {
            // Вызывается, когда происходит первое касание.
            // Возвращаем true, чтобы указать, что событие onDown было обработано.
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                // Вычисляем разницу по осям X и Y между начальным и конечным положениями касания.
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        // Если разница по оси X больше, чем по оси Y,
                        // и превышает пороговое значение, и скорость также достаточна,
                        // то считаем свайп горизонтальным (влево или вправо).
                        if (diffX > 0) {
                            // Если разница по оси X положительна, то это свайп вправо.
                            onSwipeRight()
                        } else {
                            // Если разница по оси X отрицательна, то это свайп влево.
                            onSwipeLeft()
                        }
                        result = true
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    // Если разница по оси Y больше, чем по оси X,
                    // и превышает пороговое значение, и скорость также достаточна,
                    // то считаем свайп вертикальным (вверх или вниз).
                    if (diffY > 0) {
                        // Если разница по оси Y положительна, то это свайп вниз.
                        onSwipeBottom()
                    } else {
                        // Если разница по оси Y отрицательна, то это свайп вверх.
                        onSwipeTop()
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            return result
        }


    }

    open fun onSwipeRight() {}

    open fun onSwipeLeft() {}

    open fun onSwipeTop() {}

    open fun onSwipeBottom() {}
}