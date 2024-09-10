package com.example.movingbutton

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.movingbutton.enums.ButtonPosition
import com.example.movingbutton.enums.MoveDirection
import com.example.movingbutton.utils.AudioUtil.playKeyClickSound
import com.example.movingbutton.utils.VibrateUtil.vibtate
import com.nineoldandroids.view.ViewHelper
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Created by TheFinestArtist on 2/6/15.
 */
class MovingButton : androidx.appcompat.widget.AppCompatButton {
    interface OnPositionChangedListener {
        fun onPositionChanged(action: Int, position: ButtonPosition?)
    }

    var moveDirection: MoveDirection? = null

    // Getter and Setter
    var movementLeft: Int = 0
    var movementRight: Int = 0
    var movementTop: Int = 0
    var movementBottom: Int = 0

    var offSetInner: Int = 0
    var offSetOuter: Int = 0

    var vibrationDuration: Int = 0

    var eventVolume: Int = 0

    var onPositionChangedListener: OnPositionChangedListener? = null

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.MovingButton, 0, 0)

        moveDirection = MoveDirection.entries[attr.getInt(
            R.styleable.MovingButton_mb_move_direction,
            0
        )]

        movementLeft = attr.getDimensionPixelSize(R.styleable.MovingButton_mb_movement_left, 0)
        movementRight = attr.getDimensionPixelSize(R.styleable.MovingButton_mb_movement_right, 0)
        movementTop = attr.getDimensionPixelSize(R.styleable.MovingButton_mb_movement_top, 0)
        movementBottom = attr.getDimensionPixelSize(R.styleable.MovingButton_mb_movement_bottom, 0)

        val movement = attr.getDimensionPixelSize(R.styleable.MovingButton_mb_movement, 0)
        if (movement != 0) {
            movementBottom = movement
            movementTop = movementBottom
            movementRight = movementTop
            movementLeft = movementRight
        }

        offSetInner = attr.getDimensionPixelSize(
            R.styleable.MovingButton_mb_offset_inner,
            resources.getDimensionPixelSize(R.dimen.default_offset_inner)
        )
        offSetOuter = attr.getDimensionPixelSize(
            R.styleable.MovingButton_mb_offset_outer,
            resources.getDimensionPixelSize(R.dimen.default_offset_outer)
        )

        vibrationDuration = attr.getInt(R.styleable.MovingButton_mb_vibration_duration, 0)
        eventVolume = attr.getInt(R.styleable.MovingButton_mb_event_volume, 0)

        attr.recycle()
    }

    /**
     * Touch Event
     */
    var currentPosition: ButtonPosition = ButtonPosition.ORIGIN

    private var halfWidth = 0f
    private var halfHeight = 0f

    private var centerX = 0f
    private var centerY = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                requestTouchEvent()
                positionChanged(MotionEvent.ACTION_DOWN, ButtonPosition.ORIGIN)
                soundAndVibrate()
                halfWidth = this.width.toFloat() / 2.0f
                halfHeight = this.height.toFloat() / 2.0f
                currentPosition = ButtonPosition.ORIGIN
            }

            MotionEvent.ACTION_MOVE -> {
                recalculateCenter()
                val diffX = event.x - centerX
                val diffY = centerY - event.y

                when (moveDirection) {
                    MoveDirection.ALL -> {
                        val length = sqrt(diffX.pow(2.0f) + diffY.pow(2.0f))
                        if (length > offSetOuter) if (currentPosition == ButtonPosition.ORIGIN) moveView(
                            this, getPositionForAll(diffX.toDouble(), diffY.toDouble())
                        )
                        else moveView(
                            this,
                            getDetailedPositionForAll(diffX.toDouble(), diffY.toDouble())
                        )
                        else if (length < offSetInner) moveView(this, ButtonPosition.ORIGIN)
                    }

                    MoveDirection.HORIZONTAL_VERTICAL -> {
                        val length = sqrt(diffX.pow(2.0f) + diffY.pow(2.0f))
                        if (length > offSetOuter) if (currentPosition == ButtonPosition.ORIGIN) moveView(
                            this, getPositionForHV(diffX.toDouble(), diffY.toDouble())
                        )
                        else moveView(
                            this,
                            getDetailedPositionForHV(diffX.toDouble(), diffY.toDouble())
                        )
                        else if (length < offSetInner) moveView(this, ButtonPosition.ORIGIN)
                    }

                    MoveDirection.VERTICAL -> if (diffY > offSetOuter) moveView(
                        this, ButtonPosition.UP
                    )
                    else if (diffY < -offSetOuter) moveView(this, ButtonPosition.DOWN)
                    else if (abs(diffY.toDouble()) < offSetInner) moveView(
                        this,
                        ButtonPosition.ORIGIN
                    )

                    MoveDirection.HORIZONTAL -> if (diffX > offSetOuter) moveView(
                        this, ButtonPosition.RIGHT
                    )
                    else if (diffX < -offSetOuter) moveView(this, ButtonPosition.LEFT)
                    else if (abs(diffX.toDouble()) < offSetInner) moveView(
                        this,
                        ButtonPosition.ORIGIN
                    )

                    MoveDirection.STILL -> {}
                    else -> {

                    }
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                moveView(this, ButtonPosition.ORIGIN)
                positionChanged(MotionEvent.ACTION_UP, ButtonPosition.ORIGIN)
            }
        }
        return super.onTouchEvent(event)
    }

    private fun recalculateCenter() {
        when (currentPosition) {
            ButtonPosition.ORIGIN -> {
                centerX = halfWidth
                centerY = halfHeight
            }

            ButtonPosition.RIGHT -> {
                centerX = halfWidth - movementRight
                centerY = halfHeight
            }

            ButtonPosition.RIGHT_DOWN -> {
                centerX = halfWidth - movementRight
                centerY = halfHeight - movementBottom
            }

            ButtonPosition.DOWN -> {
                centerX = halfWidth
                centerY = halfHeight - movementBottom
            }

            ButtonPosition.LEFT_DOWN -> {
                centerX = halfWidth + movementLeft
                centerY = halfHeight - movementBottom
            }

            ButtonPosition.LEFT -> {
                centerX = halfWidth + movementLeft
                centerY = halfHeight
            }

            ButtonPosition.LEFT_UP -> {
                centerX = halfWidth + movementLeft
                centerY = halfHeight + movementTop
            }

            ButtonPosition.UP -> {
                centerX = halfWidth
                centerY = halfHeight + movementTop
            }

            ButtonPosition.RIGHT_UP -> {
                centerX = halfWidth - movementRight
                centerY = halfHeight + movementTop
            }
        }
    }

    private fun moveView(v: View, position: ButtonPosition?) {
        if (position == null || currentPosition == position) return

        positionChanged(MotionEvent.ACTION_MOVE, position)
        soundAndVibrate()
        currentPosition = position

        var goalViewX = 0f
        var goalViewY = 0f
        when (position) {
            ButtonPosition.ORIGIN -> {
                goalViewX = 0f
                goalViewY = 0f
            }

            ButtonPosition.LEFT -> {
                goalViewX = -movementLeft.toFloat()
                goalViewY = 0f
            }

            ButtonPosition.RIGHT -> {
                goalViewX = movementRight.toFloat()
                goalViewY = 0f
            }

            ButtonPosition.UP -> {
                goalViewX = 0f
                goalViewY = -movementTop.toFloat()
            }

            ButtonPosition.DOWN -> {
                goalViewX = 0f
                goalViewY = movementBottom.toFloat()
            }

            ButtonPosition.LEFT_UP -> {
                goalViewX = -movementLeft.toFloat()
                goalViewY = -movementTop.toFloat()
            }

            ButtonPosition.LEFT_DOWN -> {
                goalViewX = -movementLeft.toFloat()
                goalViewY = movementBottom.toFloat()
            }

            ButtonPosition.RIGHT_UP -> {
                goalViewX = movementRight.toFloat()
                goalViewY = -movementTop.toFloat()
            }

            ButtonPosition.RIGHT_DOWN -> {
                goalViewX = movementRight.toFloat()
                goalViewY = movementBottom.toFloat()
            }
        }
        ViewHelper.setTranslationX(v, goalViewX)
        ViewHelper.setTranslationY(v, goalViewY)
    }

    private fun getAngle(diffX: Double, diffY: Double): Double {
        val length = sqrt(diffX.pow(2.0) + diffY.pow(2.0))
        var angle = asin(diffY / length)
        angle = if (diffX >= 0.0 && diffY >= 0.0) angle + 0.0
        else if (diffX < 0.0 && diffY >= 0.0) Math.PI - angle
        else if (diffX < 0.0 && diffY < 0.0) -angle + Math.PI
        else angle + 2.0 * Math.PI
        return angle * 180.0 / Math.PI
    }

    /**
     * ButtonMovement.ALL
     */
    private fun getPositionForAll(diffX: Double, diffY: Double): ButtonPosition {
        val angle = getAngle(diffX, diffY)

        return if (67.5 > angle && angle >= 22.5) ButtonPosition.RIGHT_UP
        else if (22.5 > angle || angle >= 337.5) ButtonPosition.RIGHT
        else if (337.5 > angle && angle >= 292.5) ButtonPosition.RIGHT_DOWN
        else if (292.5 > angle && angle >= 247.5) ButtonPosition.DOWN
        else if (247.5 > angle && angle >= 202.5) ButtonPosition.LEFT_DOWN
        else if (202.5 > angle && angle >= 157.5) ButtonPosition.LEFT
        else if (157.5 > angle && angle >= 112.5) ButtonPosition.LEFT_UP
        else ButtonPosition.UP
    }

    private fun getDetailedPositionForAll(diffX: Double, diffY: Double): ButtonPosition? {
        val angle = getAngle(diffX, diffY)

        return if (11.25 > angle || angle >= 348.75) ButtonPosition.RIGHT
        else if (326.25 > angle && angle >= 303.75) ButtonPosition.RIGHT_DOWN
        else if (281.25 > angle && angle >= 258.75) ButtonPosition.DOWN
        else if (236.25 > angle && angle >= 213.75) ButtonPosition.LEFT_DOWN
        else if (191.25 > angle && angle >= 168.75) ButtonPosition.LEFT
        else if (146.25 > angle && angle >= 123.25) ButtonPosition.LEFT_UP
        else if (101.25 > angle && angle >= 78.75) ButtonPosition.UP
        else if (56.25 > angle && angle >= 33.75) ButtonPosition.RIGHT_UP
        else null
    }

    /**
     * ButtonMovement.HORIZONTAL_VERTICAL
     */
    private fun getPositionForHV(diffX: Double, diffY: Double): ButtonPosition {
        val angle = getAngle(diffX, diffY)

        return if (45 > angle || angle >= 315) ButtonPosition.RIGHT
        else if (315 > angle && angle >= 225) ButtonPosition.DOWN
        else if (225 > angle && angle >= 135) ButtonPosition.LEFT
        else ButtonPosition.UP
    }

    private fun getDetailedPositionForHV(diffX: Double, diffY: Double): ButtonPosition? {
        val angle = getAngle(diffX, diffY)

        return if (22.5 > angle || angle >= 342.5) ButtonPosition.RIGHT
        else if (292.5 > angle && angle >= 247.5) ButtonPosition.DOWN
        else if (202.5 > angle && angle >= 157.5) ButtonPosition.LEFT
        else if (112.5 > angle && angle >= 67.5) ButtonPosition.UP
        else null
    }

    private fun soundAndVibrate() {
        vibtate(context, vibrationDuration)
        playKeyClickSound(context, eventVolume)
    }

    private fun positionChanged(action: Int, position: ButtonPosition) {
        if (onPositionChangedListener != null) onPositionChangedListener!!.onPositionChanged(
            action,
            position
        )
    }

    private fun requestTouchEvent() {
        val parent = parent
        parent?.requestDisallowInterceptTouchEvent(true)
    }
}
