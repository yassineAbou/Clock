package com.example.clock.timer

 interface TimerScreenActions {
    fun setHours(hours: Long)
    fun setMinutes(minutes: Long)
    fun setSeconds(seconds: Long)
    fun setTime()
    fun handleCountDownTimer()
    fun resetTimer()
    fun onChangeDone()
    fun stopService()

}