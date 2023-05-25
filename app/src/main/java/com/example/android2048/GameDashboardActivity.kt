package com.example.android2048

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android2048.databinding.ActivityMainBinding
import java.util.*

class GameDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private val colorMapper: HashMap<Int, String> = HashMap();
    private var filledCell = ArrayList<Int>()
    private var currentState = Array(4) {Array(4) {0} }
    private var updatedState = Array(4) {Array(4) {0} }


    enum class SwipeDirection {
        Top,
        Bottom,
        Left,
        Right
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        colorMapper[0] = "#CDC1B4"
        colorMapper[3] = "#eee4da"
        colorMapper[6] = "#ede0c7"
        colorMapper[12] = "#f2b178"
        colorMapper[24] = "#f59563"
        colorMapper[48] = "#f67c5f"
        colorMapper[96] = "#f65d3b"
        colorMapper[192] = "#E4CF8B"
        colorMapper[384] = "#edcc61"
        colorMapper[768] = "#EEC43A"
        colorMapper[1536] = "#97ED61"
        colorMapper[3072] = "#B02DF2"

        addSwipeGesture(binding.swipeArea)
        playFirstMove()
    }



    fun addSwipeGesture(view: View) {
        view.isClickable = true
        view.setOnTouchListener(object : OnSwipeTouchListener(this@GameDashboardActivity) {

            override fun onSwipeTop() {
                super.onSwipeTop()
                swipeAction(SwipeDirection.Top)
            }

            override fun onSwipeBottom() {
                super.onSwipeBottom()
                swipeAction(SwipeDirection.Bottom)
            }

            override fun onSwipeLeft() {
                super.onSwipeLeft()
                swipeAction(SwipeDirection.Left)
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                swipeAction(SwipeDirection.Right)
            }
        })
    }


    fun swipeAction(action: SwipeDirection) {

        saveCurrentStateTo2DArray()


        updatedState = currentState


        when(action) {
            SwipeDirection.Top -> {
                performActionTop()
            }

            SwipeDirection.Bottom -> {
                performActionBottom()
            }

            SwipeDirection.Left -> {
                performActionLeft()
            }

            SwipeDirection.Right -> {
                performActionRight()
            }
        }


        updateFreeCells()


        updatedState = currentState


        loadFromUpdatedStateToUI()


        updateUIaccordingToNumber()


        generateNewRandomNumberAfterSwipeAction()
    }


    fun updateUIaccordingToNumber() {
        binding.tv1.setBackgroundColor(Color.parseColor(colorMapper[updatedState[0][0]]))
        binding.tv2.setBackgroundColor(Color.parseColor(colorMapper[updatedState[0][1]]))
        binding.tv3.setBackgroundColor(Color.parseColor(colorMapper[updatedState[0][2]]))
        binding.tv4.setBackgroundColor(Color.parseColor(colorMapper[updatedState[0][3]]))


        binding.tv5.setBackgroundColor(Color.parseColor(colorMapper[updatedState[1][0]]))
        binding.tv6.setBackgroundColor(Color.parseColor(colorMapper[updatedState[1][1]]))
        binding.tv7.setBackgroundColor(Color.parseColor(colorMapper[updatedState[1][2]]))
        binding.tv8.setBackgroundColor(Color.parseColor(colorMapper[updatedState[1][3]]))


        binding.tv9.setBackgroundColor(Color.parseColor(colorMapper[updatedState[2][0]]))
        binding.tv10.setBackgroundColor(Color.parseColor(colorMapper[updatedState[2][1]]))
        binding.tv11.setBackgroundColor(Color.parseColor(colorMapper[updatedState[2][2]]))
        binding.tv12.setBackgroundColor(Color.parseColor(colorMapper[updatedState[2][3]]))


        binding.tv13.setBackgroundColor(Color.parseColor(colorMapper[updatedState[3][0]]))
        binding.tv14.setBackgroundColor(Color.parseColor(colorMapper[updatedState[3][1]]))
        binding.tv15.setBackgroundColor(Color.parseColor(colorMapper[updatedState[3][2]]))
        binding.tv16.setBackgroundColor(Color.parseColor(colorMapper[updatedState[3][3]]))
    }


    fun performActionTop() {

        for(row:Int in 1 until currentState.size) {
            for(col: Int in currentState[row].indices) {
                if(currentState[row-1][col] == currentState[row][col]) {

                    currentState[row-1][col] += currentState[row][col]
                    currentState[row][col] = 0
                }
            }
        }


        for(row in 3 downTo 1) {
            for(col: Int in currentState[row].indices) {

                if(currentState[row-1][col] == 0) {

                    currentState[row-1][col] = currentState[row][col]
                    currentState[row][col] = 0
                }
            }
        }

    }


    fun performActionBottom() {

        for(row:Int in currentState.size-1 downTo 1) {
            for(col: Int in currentState[row].indices) {
                if(currentState[row-1][col] == currentState[row][col]) {
                    currentState[row][col] += currentState[row-1][col]
                    currentState[row-1][col] = 0
                }
            }
        }


        for(row in 1 until currentState.size) {
            for(col: Int in currentState[row].indices) {

                if(currentState[row][col] == 0) {

                    currentState[row][col] = currentState[row-1][col]
                    currentState[row-1][col] = 0
                }
            }
        }
    }


    fun performActionLeft() {

        for(col:Int in 1 until currentState.size) {
            for(row: Int in currentState[col].indices) {
                if(currentState[row][col-1] == currentState[row][col]) {
                    currentState[row][col-1] += currentState[row][col]
                    currentState[row][col] = 0
                }
            }
        }


        for(col in 3 downTo 1) {
            for(row: Int in currentState[col].indices) {

                if(currentState[row][col-1] == 0) {

                    currentState[row][col-1] = currentState[row][col]
                    currentState[row][col] = 0
                }
            }
        }
    }



    fun performActionRight() {


        for(col:Int in currentState.size-1 downTo 1) {
            for(row: Int in currentState[col].indices) {
                if(currentState[row][col-1] == currentState[row][col]) {

                    currentState[row][col] += currentState[row][col-1]
                    currentState[row][col-1] = 0
                }
            }
        }


        for(col in 1 until currentState.size) {
            for(row: Int in currentState[col].indices) {

                if(currentState[row][col] == 0) {

                    currentState[row][col] = currentState[row][col-1]
                    currentState[row][col-1] = 0
                }
            }
        }
    }


    fun updateFreeCells() {
        filledCell.clear()

        for(row: Int in 0 until 4) {
            if(currentState[0][row] != 0) {
                filledCell.add(row + 1)
            }

            if(currentState[1][row] != 0) {
                filledCell.add(row + 5)
            }

            if(currentState[2][row] != 0) {
                filledCell.add(row + 9)
            }

            if(currentState[3][row] != 0) {
                filledCell.add(row + 13)
            }
        }
        Log.d("TAG", "filledCellCount"+filledCell.size)
    }


    fun saveCurrentStateTo2DArray() {

        currentState[0][0] = binding.tv1.text.toString().toIntWithNullHandle();
        currentState[0][1] = binding.tv2.text.toString().toIntWithNullHandle();
        currentState[0][2] = binding.tv3.text.toString().toIntWithNullHandle();
        currentState[0][3] = binding.tv4.text.toString().toIntWithNullHandle();


        currentState[1][0] = binding.tv5.text.toString().toIntWithNullHandle();
        currentState[1][1] = binding.tv6.text.toString().toIntWithNullHandle();
        currentState[1][2] = binding.tv7.text.toString().toIntWithNullHandle();
        currentState[1][3] = binding.tv8.text.toString().toIntWithNullHandle();


        currentState[2][0] = binding.tv9.text.toString().toIntWithNullHandle();
        currentState[2][1] = binding.tv10.text.toString().toIntWithNullHandle();
        currentState[2][2] = binding.tv11.text.toString().toIntWithNullHandle();
        currentState[2][3] = binding.tv12.text.toString().toIntWithNullHandle();


        currentState[3][0] = binding.tv13.text.toString().toIntWithNullHandle();
        currentState[3][1] = binding.tv14.text.toString().toIntWithNullHandle();
        currentState[3][2] = binding.tv15.text.toString().toIntWithNullHandle();
        currentState[3][3] = binding.tv16.text.toString().toIntWithNullHandle();
    }


    fun loadFromUpdatedStateToUI() {

        binding.tv1.text = updatedState[0][0].toStringWithZeroHandle()
        binding.tv2.text = updatedState[0][1].toStringWithZeroHandle()
        binding.tv3.text = updatedState[0][2].toStringWithZeroHandle()
        binding.tv4.text = updatedState[0][3].toStringWithZeroHandle()


        binding.tv5.text = updatedState[1][0].toStringWithZeroHandle()
        binding.tv6.text = updatedState[1][1].toStringWithZeroHandle()
        binding.tv7.text = updatedState[1][2].toStringWithZeroHandle()
        binding.tv8.text = updatedState[1][3].toStringWithZeroHandle()


        binding.tv9.text = updatedState[2][0].toStringWithZeroHandle()
        binding.tv10.text = updatedState[2][1].toStringWithZeroHandle()
        binding.tv11.text = updatedState[2][2].toStringWithZeroHandle()
        binding.tv12.text = updatedState[2][3].toStringWithZeroHandle()


        binding.tv13.text = updatedState[3][0].toStringWithZeroHandle()
        binding.tv14.text = updatedState[3][1].toStringWithZeroHandle()
        binding.tv15.text = updatedState[3][2].toStringWithZeroHandle()
        binding.tv16.text = updatedState[3][3].toStringWithZeroHandle()
    }


    fun generateNewRandomNumberAfterSwipeAction() {
        var emptyCell = ArrayList<Int>()

        for (cellId in 1..16) {
            if((!filledCell.contains(cellId))) {
                emptyCell.add(cellId)
            }
        }

        if(emptyCell.size == 0) {
            Toast.makeText(this, "Game over! Start new game", Toast.LENGTH_SHORT).show()
            return;
        }
        val r = Random()
        val randIndex = r.nextInt(emptyCell.size)
        val cellId = emptyCell[randIndex]

        var tvSelected: TextView?
        tvSelected = when(cellId) {
            1-> binding.tv1
            2-> binding.tv2
            3-> binding.tv3
            4-> binding.tv4
            5-> binding.tv5
            6-> binding.tv6
            7-> binding.tv7
            8-> binding.tv8
            9-> binding.tv9
            10-> binding.tv10
            11-> binding.tv11
            12-> binding.tv12
            13-> binding.tv13
            14-> binding.tv14
            15-> binding.tv15
            16-> binding.tv16
            else -> { binding.tv1}
        }
        playGame(cellId, tvSelected, generateCellRandomNumber())
    }


    fun restartGame() {
        filledCell.clear()

        for (cellId in 1..16) {
            var tvSelected: TextView?
            tvSelected = when(cellId) {
                1-> binding.tv1
                2-> binding.tv2
                3-> binding.tv3
                4-> binding.tv4
                5-> binding.tv5
                6-> binding.tv6
                7-> binding.tv7
                8-> binding.tv8
                9-> binding.tv9
                10-> binding.tv10
                11-> binding.tv11
                12-> binding.tv12
                13-> binding.tv13
                14-> binding.tv14
                15-> binding.tv15
                16-> binding.tv16
                else -> { binding.tv1}
            }
            tvSelected?.text = ""
            tvSelected?.setBackgroundResource(R.color.cellDefaultColor)
        }

        playFirstMove()
    }


    fun generateCellRandomNumber(): Int {
        return if (Math.random() < 0.9) 3 else 6
    }


    fun playFirstMove() {
        var emptyCell = ArrayList<Int>()

        for (cellId in 1..16) {
            if((!filledCell.contains(cellId))) {
                emptyCell.add(cellId)
            }
        }
        val ints = Random().ints(1, emptyCell.size).distinct().limit(2).toArray()

        val cellId = emptyCell[ints[0]]

        var tvSelected: TextView?
        tvSelected = when(cellId) {
            1-> binding.tv1
            2-> binding.tv2
            3-> binding.tv3
            4-> binding.tv4
            5-> binding.tv5
            6-> binding.tv6
            7-> binding.tv7
            8-> binding.tv8
            9-> binding.tv9
            10-> binding.tv10
            11-> binding.tv11
            12-> binding.tv12
            13-> binding.tv13
            14-> binding.tv14
            16-> binding.tv16
            else -> { binding.tv1}
        }

        playGame(cellId, tvSelected, generateCellRandomNumber())

        val cellId2 = emptyCell[ints[1]]

        var tvSelected1: TextView?
        tvSelected1 = when(cellId2) {
            1-> binding.tv1
            2-> binding.tv2
            3-> binding.tv3
            4-> binding.tv4
            5-> binding.tv5
            6-> binding.tv6
            7-> binding.tv7
            8-> binding.tv8
            9-> binding.tv9
            10-> binding.tv10
            11-> binding.tv11
            12-> binding.tv12
            13-> binding.tv13
            14-> binding.tv14
            15-> binding.tv15
            16-> binding.tv16
            else -> { binding.tv1}
        }

        playGame(cellId2, tvSelected1, generateCellRandomNumber())
    }


    fun playGame(cellId: Int, tvSelected: TextView, value: Int) {
        filledCell.add(cellId)
        tvSelected.text = value.toString()
        tvSelected.setBackgroundColor(Color.parseColor(colorMapper[value]))
    }

    fun launchNewGame(view: android.view.View) {
        restartGame()
    }
}

fun String.toIntWithNullHandle(): Int {
    if(this.contentEquals("")) {
        return 0
    }
    return this.toInt()
}


fun Int.toStringWithZeroHandle(): String {
    if(this == 0) {
        return ""
    }
    return this.toString()
}