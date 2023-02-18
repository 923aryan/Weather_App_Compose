import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.reduce

suspend fun main()
{
    println("hello")
    val result = (2..5).asFlow()
        .reduce { a, b -> println("$a $b"); a+b }
    println("result $result")

}