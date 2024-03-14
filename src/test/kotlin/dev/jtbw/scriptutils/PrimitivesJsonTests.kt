package dev.jtbw.scriptutils

import com.squareup.moshi.JsonEncodingException
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class PrimitivesJsonTests {
  @Test
  fun `nested maps`() {
    val json =
      """
    {
      "people": {
        "Alice": {
          "age": 30,
          "city": "Seattle"
        },
        "Bob": {
          "age": 31,
          "city": "Minneapolis",
          "male": true,
          "alive": "true",
          "job": null
        },
        "Doug": "dog"
      }
    }
    """

    with(PrimitivesJson) {
      val data = deserializeJsonObjectToPrimitives(json)
      data["people"]["Alice"]["age"] shouldBe 30.0
      data["people"]["Bob"]["city"] shouldBe "Minneapolis"
      data["people"]["Bob"]["male"] shouldBe true
      data["people"]["Bob"]["alive"] shouldBe "true"
      data["people"]["Bob"]["job"] shouldBe null

      data["people"]["Charles"] shouldBe null
      var exception = runCatching { data["people"]["Charles"]["age"] }.exceptionOrNull()!!
      assertEquals(NullPointerException::class.java, exception::class.java)

      data["people"]["Doug"] shouldBe "dog"
      exception = runCatching { data["people"]["Doug"]["age"] }.exceptionOrNull()!!
      assertEquals(ClassCastException::class.java, exception::class.java)
      assertTrue(
        exception.message!!.startsWith(
          """cannot get key "age", because: class java.lang.String cannot be cast to class java.util.Map"""
        )
      )
    }
  }

  @Test
  fun `json list`() {
    val json =
      """
    [
        {
          "name": "Alice",
          "age": 30,
          "city": "Seattle"
        },
        "in-between",
        {
          "name": "Bob",
          "age": 31,
          "city": "Minneapolis"
        }
    ]
    """
        .trimIndent()

    with(PrimitivesJson) {
      val data = deserializeJsonListToPrimitives(json)
      data[0]["name"]
    }
  }

  @Test
  fun `invalid json`() {
    val json =
      """
    {
      "name": "Alice",
      42: true,
    },
    """
        .trimIndent()

    with(PrimitivesJson) {
      assertEquals(
        JsonEncodingException::class.java,
        runCatching { deserializeJsonObjectToPrimitives(json) }.exceptionOrNull()!!::class.java
      )
    }
  }

  @Test
  fun `map modification and serialization`() {
    val json =
      """
    {
      "people": {
        "Alice": {
          "age": 30,
          "city": "Seattle"
        },
        "Bob": {
          "age": 31,
          "city": "Minneapolis"
        }
      }
    }
    """

    with(PrimitivesJson) {
      val map = deserializeJsonObjectToPrimitives(json)

      map["people"]["Alice"]["age"] = 99
      serializeToJson(map) shouldBe
        """
       {"people":{"Alice":{"age":99,"city":"Seattle"},"Bob":{"age":31.0,"city":"Minneapolis"}}}
    """
          .trimIndent()
    }
  }

  @Test
  fun `list modification and serialization`() {
    val json =
      """
    [
        {
          "age": 30,
          "city": "Seattle"
        },
        {
          "age": 31,
          "city": "Minneapolis",
          "friends": ["Alice"]
        }
    ]
    """

    with(PrimitivesJson) {
      val map = deserializeJsonListToPrimitives(json)

      val bob = map[1]
      bob["friends"] = bob["friends"] as List<String> + "Charlie"

      serializeToJson(map) shouldBe
        """
       [{"age":30.0,"city":"Seattle"},{"age":31.0,"city":"Minneapolis","friends":["Alice","Charlie"]}]
    """
          .trimIndent()
    }
  }
}
