# CoroutineEx

# 코루틴

Created: 2022년 3월 14일 오후 3:36

# Kotlin과 코루틴

코루틴의 주요 기능 중 하나는 상태를 저장하여 중단했다가 재개할 수 있으며, 코루틴은 실행되거나 실행되지 않을 수 있다. → 멀티 스레드 사용에 매우 유용

| Job | 취소 가능한 작업 단위(예: launch() 함수로 만든 작업 단위)입니다. |
| --- | --- |
| Dispatcher | 코루틴이 사용할 스레드를 결정합니다. Main디스패처는 항상 기본 스레드에서     코루틴을 실행하지만 Default나 IO, Unconfined와 같은 디스패처는 다른 스레드를 사용합니다. * 코루틴 스코프만 |
| CoroutineScope | launch() 및 async()와 같은 새 코루틴을 만드는 데 사용되는 함수는 CoroutineScope를 확장합니다.  *필요할때만 열고 완료되면 닫아줌 ( 네트워크 등 ) |
| GlobalScope | GlobalScope는 앱이 실행되는 한 내부의 코루틴이 실행되도록 허용합니다.             *앱의 생명주기와 함께 동작 |

# 코루틴의 상태관리

> **cancel**
> 

코루틴의 동작을 멈추는 상태관리 매서드. 

하나의 스코프 안에 여러 코루틴이 존재하는 경우 하위 코루틴까지 모두 중지

예시 코드)

```kotlin
val job = CoroutineScope(Dispatchers.Default).launch{
	val job1 = launch{
                for (i in 0..5){
                    delay(500)
                    Log.e("Jaemu>>","코루틴 $i")
                }
            }
}

button.setOnClickListener {
            job.cancel()
        }

// 위 코드를 실행중 버튼을 누르면 job과 그 하위의 job1도 같이 종료

```

```kotlin
val job = CoroutineScope(Dispatchers.Default).launch{
            launch{
                for (i in 0..5){
                    delay(500) // 1. delay는 yield와 같이 다른 코루틴에 실행을 양보
                    Log.e("Jaemu>>","코루틴 $i")
                }
            }

            launch{
                for (i in 6..10){
                    delay(500)
                    Log.e("Jaemu>>","코루틴 $i")
                }
            }
        }

button.setOnClickListener {
            job.cancel()
        }

// 2. 따라서 위 코드의 결과는

E/Jaemu>>: 코루틴 0
E/Jaemu>>: 코루틴 6
E/Jaemu>>: 코루틴 7
E/Jaemu>>: 코루틴 1
E/Jaemu>>: 코루틴 2
E/Jaemu>>: 코루틴 8
E/Jaemu>>: 코루틴 9
E/Jaemu>>: 코루틴 3
E/Jaemu>>: 코루틴 10
E/Jaemu>>: 코루틴 4
E/Jaemu>>: 코루틴 5

//3 . 서로 양보만 하며 실행시간이 다르니 순서도 뒤죽박죽 :(
```

> join
> 

코루틴 내부에 여러 `launch`블록이 있는 경우 실행 순서를 정할 수 없음.

순서를 정해야 한다면 `join()`을 사용해 순차적으로 실행.

예시 코드)

```kotlin
val job = CoroutineScope(Dispatchers.Default).launch{
            launch{
                for (i in 0..5){
                    delay(500) 
                    Log.e("Jaemu>>","코루틴 $i")
                }
            }.join() // 1. join 선언시 해당 launch블록이 먼저 실행

            launch{
                for (i in 6..10){
                    delay(500)
                    Log.e("Jaemu>>","코루틴 $i")
                }
            }
        }

button.setOnClickListener {
            job.cancel()
        }

// 2. 따라서 위 코드의 결과는

E/Jaemu>>: 코루틴 0
E/Jaemu>>: 코루틴 1
E/Jaemu>>: 코루틴 2
E/Jaemu>>: 코루틴 3
E/Jaemu>>: 코루틴 4
E/Jaemu>>: 코루틴 5
E/Jaemu>>: 코루틴 6
E/Jaemu>>: 코루틴 7
E/Jaemu>>: 코루틴 8
E/Jaemu>>: 코루틴 9
E/Jaemu>>: 코루틴 10

//3 . 순차적으로 실행되는 모습
```

> async
> 

코루틴 스코프의 결과를 받아와 사용할 수 있는데, 이를 `async()`로 할 수 있다.

네트워크 작업과 같이 시간이 다소 소요되는 작업을 `async()` 로 처리하고

`await()`를 사용해 결과를 받아옴. 이때, `async()` 작업이 끝나고 `await()` 가 호출.

예시 코드)

```kotlin
val formatter = DateTimeFormatter.ISO_LOCAL_TIME

val job = CoroutineScope(Dispatchers.Default).async{ 
	val as1 = async{ // 1. async를 사용해 500 밀리세컨드를 기다린 후 200 반환
		Log.e("Jaemu>>", "as1 start : "+formatter.format(LocalDateTime.now()))
		delay(500)
    Log.e("Jaemu>>", "as1 end : "+formatter.format(LocalDateTime.now()))
    200
  }

  val as2 = async{ // 2. async를 사용해 1000 밀리세컨드를 기다린 후 300 반환
    Log.e("Jaemu>>", "as2 start : "+formatter.format(LocalDateTime.now()))
    delay(1000)
    Log.e("Jaemu>>", "as2 end : "+formatter.format(LocalDateTime.now()))
    300
  }
	
	// 3. 1번 테스크가 먼저 끝나지만 as2.await()가 있으므로 2번 테스크가 끝날때까지 기다리고
	// 결과를 출력
  Log.e("Jaemu>>","${as1.await()} + ${as2.await()}")
}       

// 결과
E/Jaemu>>: as2 start : 21:23:32.174
E/Jaemu>>: as1 start : 21:23:32.174
E/Jaemu>>: as1 end : 21:23:32.676
E/Jaemu>>: as2 end : 21:23:33.179
E/Jaemu>>: 200 + 300
```

> suspend
> 

suspend 키워드가 코루틴 안에서 사용되면 이전까지의 코드의 실행이 멈추고 suspend 함수가 완료되고 멈춰있던 코루틴 스코프의 다음 코드가 실행된다.

이게 결국 코루틴 스코프 안에서 자동으로 백그라운드 스레드처럼 동작한다고 한다.

**코루틴을 사용하기 전에 Thread로 코딩해보신 분들은 알것이다... 자동으로 스레드를 관리해준다는게 얼마나 편리한것인지.. 에욱🤢*

예시 코드)

```kotlin
suspend fun suspendFunc(){
            for(i in 0..10){
                Log.e("Jaemu>>","$i")
            }
}

CoroutineScope(Dispatchers.Main).launch {
	// suspend 함수가 먼저 실행 된 후, 나머지 launch블록들이 실행된다.
	launch{
		for (i in 11..20){
			delay(500)
      Log.e("Jaemu>>","코루틴 $i")
		}
	}
	
	// suspend함수
  suspendFunc()

  launch{
     for (i in 21..30){
        delay(500)
        Log.e("Jaemu>>","코루틴 $i")
      }
   }
}

// 결과 - 길어서 끝에 자름.
E/Jaemu>>: 0
E/Jaemu>>: 1
E/Jaemu>>: 2
E/Jaemu>>: 3
E/Jaemu>>: 4
E/Jaemu>>: 5
E/Jaemu>>: 6
E/Jaemu>>: 7
E/Jaemu>>: 8
E/Jaemu>>: 9
E/Jaemu>>: 10       // <- suspend 함수가 끝나야 다른 launch블록들이 실행되는 모습
E/Jaemu>>: 코루틴 11
E/Jaemu>>: 코루틴 21
E/Jaemu>>: 코루틴 12
E/Jaemu>>: 코루틴 22
E/Jaemu>>: 코루틴 13
E/Jaemu>>: 코루틴 23
E/Jaemu>>: 코루틴 14
E/Jaemu>>: 코루틴 24
E/Jaemu>>: 코루틴 15
E/Jaemu>>: 코루틴 25        
```