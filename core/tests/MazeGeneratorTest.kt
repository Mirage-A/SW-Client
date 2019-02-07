import com.mirage.model.datastructures.IntPair
import com.mirage.model.datastructures.get
import com.mirage.model.datastructures.set
import com.mirage.model.scene.MazeGenerator
import org.junit.Test
import java.util.*

internal class MazeGeneratorTest {

    @Test
    fun generateMaze() {
        for (i in 1 until 50) {
            val scene = MazeGenerator.generateMaze(i, i)
            val stack = Stack<IntPair>()
            val visited = Array(i * 2 + 1) { BooleanArray(i * 2 + 1) { false } }
            stack.push(IntPair(1, 1))
            while (stack.isNotEmpty()) {
                val point = stack.pop()
                visited[point] = true
                if (point.x > 0 && !visited[point.left()]) stack.push(point.left())
                if (point.x < scene.width - 1 && !visited[point.right()]) stack.push(point.right())
                if (point.y > 0 && !visited[point.bottom()]) stack.push(point.bottom())
                if (point.y < scene.height - 1 && !visited[point.top()]) stack.push(point.top())
            }
            assert(visited[scene.width - 2][scene.height - 2])
        }
    }
}