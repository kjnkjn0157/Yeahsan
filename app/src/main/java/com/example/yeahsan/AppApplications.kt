package com.example.yeahsan

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.yeahsan.data.api.model.DoorListVO
import com.example.yeahsan.service.beacon.BeaconService
import com.example.yeahsan.service.`interface`.ContentResult
import com.example.yeahsan.service.location.LocationService
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class AppApplications : Application() {

    override fun onCreate() {
        super.onCreate()

        //  #1. Single Thread 방식
        doSingleThread()

        //  #2. Mutex 방식
//        doMutex()

        //  #3. Actor 방식
//        doActor()
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    /**
     * Beacon Service */
    fun startBeaconService(isBeaconServiceRunning : Boolean) {
        if (!isBeaconServiceRunning) {
            val intent = Intent(applicationContext, BeaconService::class.java)
            intent.action = "startBeacon"
            startService(intent)
            Toast.makeText(this.applicationContext, "Beacon service started", Toast.LENGTH_SHORT).show()
        }
    }

    fun stopBeaconService(isBeaconServiceRunning : Boolean) {
        if (isBeaconServiceRunning) {
            val intent = Intent(applicationContext, BeaconService::class.java)
            intent.action = "stopBeacon"
            startService(intent)
            Toast.makeText(this, "Beacon service stopped", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * location service */
    fun startLocationService() {
        val intent = Intent(applicationContext, LocationService::class.java)
        intent.action = "startLocation"
        startService(intent)
        Toast.makeText(this.applicationContext, "Location service started", Toast.LENGTH_SHORT).show()
    }

    fun stopLocationService() {
        val intent = Intent(applicationContext, BeaconService::class.java)
        intent.action = "stopLocation"
        startService(intent)
        Toast.makeText(this, "Location service stopped", Toast.LENGTH_SHORT).show()
    }





    /**
     * <single thread 가 아닌(동기화처리 없이) 다중 스레드로 접근했을 때 생기는 문제 시 해결방안>
     * 1. Volatiles are of no help : @Volatile , 결과 보장 X
     * 2. Thread-safe data structures : AtomicInteger class 존재, 복잡한 연산에서 사용 불가
     * 3. Thread confinement fine-gained : 862 ms 전용 context 만들고 이 스레드에서만 변수에 대한 접근이 가능하도록 제한
     * 4. Thread confinement coarse-gained : 48 ms --> 윗단계의 불필요한 context 지우고 실행
     * 5. Mutual exclusion : 419 ms --> critical section으로 보호하고 동시에 실행되지않도록
     * 6. Actors : 366 ms
     */
    val singleContext = newSingleThreadContext("ContentContext")
    val mutex = Mutex()
    var contents: ArrayList<DoorListVO> = arrayListOf()

    suspend fun GlobalScope.addContent(action: suspend () -> Unit) {// job 100개를 실행하고 각각 매개변수에서 전달받은 action을 1000번 수행하는 시간을 측정
        val time = measureTimeMillis {
            val n = 100
            val k = 1000
            val jobs = List(n) {
                launch {
                    repeat(k) { action() }
                }
            }
            jobs.forEach { it.join() }
        }
        println(">>> time : $time ms")
    }

    suspend fun CoroutineScope.addContent(action: suspend () -> Unit) { // job 100개를 실행하고 각각 매개변수에서 전달받은 action을 1000번 수행하는 시간을 측정
        val time = measureTimeMillis {
            val n = 100
            val k = 1000
            val jobs = List(n) {
                launch {
                    repeat(k) { action() }
                }
            }
            jobs.forEach {
                it.join() // 이 작업이 완료 될 떄까지 코루틴 일시 중단
            }
        }
        println(">>> time : $time ms")
    }

    private fun doSingleThread() {
        runBlocking {
            println(">>> contents.size : 0")
            //  #1-1. Thread confinement fine-gained
//            GlobalScope.addContent {
//                withContext(singleContext) {
//                    val content = DoorListVO(1, "code", "name", "hint", "image", 127, 37, arrayListOf(), arrayListOf())
//                    contents.add(content)
//                }
//            }

            //  #1-2. SingleContext
            CoroutineScope(singleContext).addContent {
                val content = DoorListVO(1, "code", "name", "hint", "image", "","","", "", arrayListOf(), arrayListOf())
                contents.add(content)
            }
            println(">>> contents.size : ${contents.size}")
        }
    }

    val contentResult = object : ContentResult {
        override fun onContentReceived(content: DoorListVO) {
            //  단일 결과
            //addCount(content)
            Log.e("TAG", "content data ::: $content")
        }
    }
    private fun doMutex() {
        runBlocking {
            println(">>> contents.size : 0")
            //  #2. Mutex 속도 중간
            GlobalScope.addContent {
//                mutex.lock()
//                try {
//                    val content = DoorListVO(1, "code", "name", "hint", "image", 127, 37, arrayListOf(), arrayListOf())
//                    contents.add(content)
//                }
//                finally {
//                    mutex.unlock()
//                }

                mutex.withLock { //위 try finally와 같은 내용
                    val content = DoorListVO(1, "code", "name", "hint", "image", "","","", "", arrayListOf(), arrayListOf())
                    contents.add(content)
                }
            }

            //synchronized , ReentrantLock

            println(">>> contents.size : ${contents.size}")
        }
    }

    sealed class ContentMsg
    object AddContent: ContentMsg()
    class GetContent(val response: CompletableDeferred<DoorListVO>) : ContentMsg()

    fun CoroutineScope.contentActor() = actor<ContentMsg> {
        val content = DoorListVO(1, "code", "name", "hint", "image", "","","", "", arrayListOf(), arrayListOf())
        for (msg in channel) { // iterate over incoming messages
            when (msg) {
                is AddContent -> contents.add(content)
                is GetContent -> msg.response.complete(content)
                else -> {}
            }
        }
    }

    private fun doActor() {
        runBlocking {
            println(">>> contents.size : 0")

            val content = contentActor()
            GlobalScope.addContent {
                content.send(AddContent)
            }

            val response: CompletableDeferred<DoorListVO> = CompletableDeferred()
            content.send(GetContent(response))
            content.close()

            println(">>> contents.size : ${contents.size}")
        }
    }
}