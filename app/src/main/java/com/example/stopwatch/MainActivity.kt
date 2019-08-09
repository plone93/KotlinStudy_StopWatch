package com.example.stopwatch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private var time = 0 //시간을 계산할 변수
    private var timerTask: Timer? = null //코틀린은 null값을 넣기위해선 변수명뒤에 ?를 붙여야함
    private var isRunning = false // 현재 상태를 나타내는 boolean 변수
    private var lap = 1 // 몇번째인지 표기하는 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //id가 fab인 아이콘을 클릭하면 실행
        fab.setOnClickListener {
            isRunning = !isRunning // 현재 상태의 반대로 전환  false > true  , true > false
            if(isRunning) {
                start() // isRunning이 true라면 start()메서드를 실행
            } else {
                pause() // isRunning이 false라면 pause()메서드를 실행
            }
        }

        //id가 labButton인 아이콘을 클릭하면 실행
        labButton.setOnClickListener {
            recordLapTime()
        }

        //id가 resetFab인 아이콘을 클릭하면 실행
        resetFab.setOnClickListener {
            reset() // 리셋 메서드 실행
        }
    }

    //시작
    private  fun start(){
        fab.setImageResource(R.drawable.ic_pause_black_24dp) // 시작이 되면 일시정지 아이콘으로 교체
        
        timerTask = timer(period = 10){// 0.01초단위로 실행   1000 = 1초
            time++
            val sec = time / 100
            val milli = time % 100
            runOnUiThread{
                secTextView.text = "$sec" // 계산된 값을 text에 입력
                milliTextView.text = "$milli"
            }
        }
    }

    //일시정지
    private fun pause(){
        fab.setImageResource(R.drawable.ic_play_arrow_black_24dp)//일시정지가 되면 재생 아이콘으로 교체
        timerTask?.cancel() //타이머 취소
    }

    //랩타임 기록
    private fun recordLapTime() {
        val lapTime = this.time //현재시간을 저장
        val textView = TextView(this) // 동적인 텍스트뷰 생성
        textView.text = "$lap LAB : ${lapTime / 100}.${lapTime % 100}" //// 1LAB : 5.35와 같은 형태로 설정해서 text에 입력
        //맨위에 랩타임 추가
        lapLayout.addView(textView, 0) //리니어 레이아웃에 text를 쌓아올림
        lap++ // 변수 증가
    }
    
    //타이머 초기화
    private fun reset(){
        timerTask?.cancel()
        
        //모든변수 초기화
        time = 0 
        isRunning = false
        fab.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        secTextView.text = "0" // secTextView라는 id를 가진 텍스트뷰의 값을 0으로 설정
        milliTextView.text = "00" // milliTextView라는 id를 가진 텍스트뷰의 값을 00으로 설정
        
        //모든 랩타임 제거
        lapLayout.removeAllViews() // 리니어 레이아웃의 모든(쌓아올린) 뷰 제거
        lap = 1
    }
}
