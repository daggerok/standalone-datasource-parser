package daggerok;

import daggerok.extensions.Args;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Args extension test")
class ArgsTest {

  @Test
  @DisplayName("Args.firstBoolean extension test")
  void firstBoolean() {

    final String[] args = {
        "--a-boolean=true",
        "--list-of-booleans=true,false,true,true,false,false",
    };

    final Map<String, List<String>> props = Args.props(args);

    assertAll(
        "firstBoolean",

        () -> {

          final boolean firstBoolean = Args.firstBoolean(props, "a-boolean");

          assertAll(
              "a-boolean",
              () -> assertNotNull(firstBoolean),
              () -> assertTrue(firstBoolean)
          );
        },

        () -> {

          final boolean firstBoolean = Args.firstBoolean(props, "a-boolean-not-found");

          assertAll(
              "a-boolean-not-found",
              () -> assertNotNull(firstBoolean),
              () -> assertFalse(firstBoolean)
          );
        },

        () -> {

          final boolean firstBoolean = Args.firstBoolean(props, "a-boolean", false);

          assertAll(
              "a-boolean or false",
              () -> assertNotNull(firstBoolean),
              () -> assertTrue(firstBoolean)
          );
        },

        () -> {

          final boolean firstBoolean = Args.firstBoolean(props, "a-boolean-not-found", true);

          assertAll(
              "a-boolean-not-found or true",
              () -> assertNotNull(firstBoolean),
              () -> assertTrue(firstBoolean)
          );
        },

        () -> {

          final boolean firstBoolean = Args.firstBoolean(props, "list-of-booleans");

          assertAll(
              "list-of-booleans",
              () -> assertNotNull(firstBoolean),
              () -> assertTrue(firstBoolean)
          );
        }
    );
  }

  @Test
  @DisplayName("Args.anyBooleans extension test")
  void anyBooleans() {

    final String[] args = {
        "--a-boolean=true",
        "--list-of-booleans=true,false,true,true,false,false",
    };

    final Map<String, List<String>> props = Args.props(args);

    assertAll(
        "anyBooleans",

        () -> {

          final List<Boolean> listOfBooleans = Args.anyBooleans(props, "list-of-booleans");

          assertAll(
              "list-of-booleans",
              () -> assertEquals(6, listOfBooleans.size()),
              () -> assertTrue(listOfBooleans.get(0)),
              () -> assertFalse(listOfBooleans.get(1)),
              () -> assertTrue(listOfBooleans.get(2)),
              () -> assertTrue(listOfBooleans.get(3)),
              () -> assertFalse(listOfBooleans.get(4)),
              () -> assertFalse(listOfBooleans.get(5))
          );
        },

        () -> {

          final List<Boolean> listOfSingleBoolean = Args.anyBooleans(props, "a-boolean");

          assertAll(
              "a-boolean",
              () -> assertEquals(1, listOfSingleBoolean.size()),
              () -> assertTrue(listOfSingleBoolean.get(0))
          );
        },

        () -> {

          final List<Boolean> emptyList = Args.anyBooleans(props, "key-not-found");

          assertAll(
              "key-not-found",
              () -> assertNotNull(emptyList),
              () -> assertEquals(0, emptyList.size())
          );
        },

        () -> {

          final List<Boolean> defaultList = Args.anyBooleans(props, "key-not-found", singletonList(true));

          assertAll(
              "key-not-found",
              () -> assertNotNull(defaultList),
              () -> assertEquals(1, defaultList.size()),
              () -> assertTrue(defaultList.get(0))
          );
        }
    );
  }

  @Test
  @DisplayName("Args.firstLong extension test")
  void firstLong() {

    final String[] args = {
        "--a-long=-1234567890",
        "--list-of-longs=-11234567890,1,0,3",
    };

    final Map<String, List<String>> props = Args.props(args);

    assertAll(
        "firstLong",

        () -> {

          final Long firstLong = Args.firstLong(props, "a-long");

          assertAll(
              "a-long",
              () -> assertNotNull(firstLong),
              () -> assertEquals(-1234567890L, firstLong.intValue())
          );
        },

        () -> {

          final Long firstLong = Args.firstLong(props, "a-long-not-found");

          assertAll(
              "a-long-not-found",
              () -> assertNotNull(firstLong),
              () -> assertEquals(0L, firstLong.intValue())
          );
        },

        () -> {

          final Long firstLong = Args.firstLong(props, "a-long", -1);

          assertAll(
              "a-long or -1",
              () -> assertNotNull(firstLong),
              () -> assertEquals(-1234567890L, firstLong.longValue())
          );
        },

        () -> {

          final Long firstLong = Args.firstLong(props, "a-long-not-found", -2);

          assertAll(
              "a-long-not-found or -2",
              () -> assertNotNull(firstLong),
              () -> assertEquals(-2, firstLong.longValue())
          );
        },

        () -> {

          final Long firstLong = Args.firstLong(props, "list-of-longs");

          assertAll(
              "list-of-longs",
              () -> assertNotNull(firstLong),
              () -> assertEquals(-11234567890L, firstLong.longValue())
          );
        }
    );
  }

  @Test
  @DisplayName("Args.anyLongs extension test")
  void anyLongs() {

    final String[] args = {
        "--a-long=-1234567890",
        "--list-of-longs=-11234567890,1,0,3",
    };

    final Map<String, List<String>> props = Args.props(args);

    assertAll(
        "anyLongs",

        () -> {

          final List<Long> listOfLongs = Args.anyLongs(props, "list-of-longs");

          assertAll(
              "list-of-longs",
              () -> assertEquals(4, listOfLongs.size()),
              () -> assertEquals(-11234567890L, listOfLongs.get(0).longValue()),
              () -> assertEquals(1, listOfLongs.get(1).longValue()),
              () -> assertEquals(0L, listOfLongs.get(2).longValue()),
              () -> assertEquals(3L, listOfLongs.get(3).longValue())
          );
        },

        () -> {

          final List<Long> listOfSingleLong = Args.anyLongs(props, "a-long");

          assertAll(
              "a-long",
              () -> assertEquals(1, listOfSingleLong.size()),
              () -> assertEquals(-1234567890L, listOfSingleLong.get(0).longValue())
          );
        },

        () -> {

          final List<Long> emptyList = Args.anyLongs(props, "key-not-found");

          assertAll(
              "key-not-found",
              () -> assertNotNull(emptyList),
              () -> assertEquals(0, emptyList.size())
          );
        },

        () -> {

          final List<Long> emptyList = Args.anyLongs(props, "key-not-found", singletonList(-3L));

          assertAll(
              "key-not-found",
              () -> assertNotNull(emptyList),
              () -> assertEquals(1, emptyList.size()),
              () -> assertEquals(-3L, emptyList.get(0).longValue())
          );
        }
    );
  }

  @Test
  @DisplayName("Args.firstInteger extension test")
  void firstInteger() {

    final String[] args = {
        "--an-integer=1",
        "--list-of-integers=-1,0,1,2,3",
    };

    final Map<String, List<String>> props = Args.props(args);

    assertAll(
        "firstInteger",

        () -> {

          final Integer firstInteger = Args.firstInteger(props, "an-integer");

          assertAll(
              "an-integer",
              () -> assertNotNull(firstInteger),
              () -> assertEquals(1, firstInteger.intValue())
          );
        },

        () -> {

          final Integer firstInteger = Args.firstInteger(props, "an-integer", -1);

          assertAll(
              "an-integer or -1",
              () -> assertNotNull(firstInteger),
              () -> assertEquals(1, firstInteger.intValue())
          );
        },

        () -> {

          final Integer notFoundInteger = Args.firstInteger(props, "not-found-key");

          assertAll(
              "not-found-key",
              () -> assertNotNull(notFoundInteger),
              () -> assertEquals(0, notFoundInteger.intValue())
          );
        },

        () -> {

          final Integer notFoundInteger = Args.firstInteger(props, "not-found-key", 42);

          assertAll(
              "not-found-key or 42",
              () -> assertNotNull(notFoundInteger),
              () -> assertEquals(42, notFoundInteger.intValue())
          );
        }
    );
  }

  @Test
  @DisplayName("Args.anyIntegers extension test")
  void anyIntegers() {

    final String[] args = {
        "--an-integer=1",
        "--list-of-integers=-1,0,1,2,3",
    };

    final Map<String, List<String>> props = Args.props(args);

    assertAll(
        "anyIntegers",

        () -> {

          final List<Integer> listOfIntegers = Args.anyIntegers(props, "list-of-integers");

          assertAll(
              "list-of-integers",
              () -> assertEquals(5, listOfIntegers.size()),
              () -> assertEquals(-1, listOfIntegers.get(0).intValue()),
              () -> assertEquals(0, listOfIntegers.get(1).intValue()),
              () -> assertEquals(1, listOfIntegers.get(2).intValue()),
              () -> assertEquals(2, listOfIntegers.get(3).intValue()),
              () -> assertEquals(3, listOfIntegers.get(4).intValue())
          );
        },

        () -> {

          final List<Integer> listOfSingleInteger = Args.anyIntegers(props, "an-integer");

          assertAll(
              "an-integer",
              () -> assertEquals(1, listOfSingleInteger.size()),
              () -> assertEquals(1, listOfSingleInteger.get(0).intValue())
          );
        },

        () -> {

          final List<Integer> emptyList = Args.anyIntegers(props, "key-not-found");

          assertAll(
              "key-not-found",
              () -> assertNotNull(emptyList),
              () -> assertEquals(0, emptyList.size())
          );
        },

        () -> {

          final List<Integer> emptyList = Args.anyIntegers(props, "key-not-found", asList(-3, 0));

          assertAll(
              "key-not-found",
              () -> assertNotNull(emptyList),
              () -> assertEquals(2, emptyList.size()),
              () -> assertEquals(-3, emptyList.get(0).intValue()),
              () -> assertEquals(0, emptyList.get(1).intValue())
          );
        }
    );
  }

  @Test
  @DisplayName("Args.firstString extension test")
  void firstString() {

    final String[] args = {
        "--a-string=string-value",
        "--list-of-strings=string-value-1,string-value-2,string-value-3",
    };

    final Map<String, List<String>> props = Args.props(args);


    assertAll(
        "firstString",

        () -> {

          final String firstString = Args.firstString(props, "a-string");

          assertAll(
              "a-string",
              () -> assertNotNull(firstString),
              () -> assertEquals("string-value", firstString)
          );
        },

        () -> {

          final String firstString = Args.firstString(props, "a-string", "empty");

          assertAll(
              "a-string or empty",
              () -> assertNotNull(firstString),
              () -> assertEquals("string-value", firstString)
          );
        },

        () -> {

          final String firstOfListOfStrings = Args.firstString(props, "list-of-strings", "empty");

          assertAll(
              "list-of-strings",
              () -> assertNotNull(firstOfListOfStrings),
              () -> assertEquals("string-value-1", firstOfListOfStrings)
          );
        },

        () -> {

          final String notFoundString = Args.firstString(props, "not-found-key", "not-found-value");

          assertAll(
              "not-found-key",
              () -> assertNotNull(notFoundString),
              () -> assertEquals("not-found-value", notFoundString)
          );
        },

        () -> {

          final String notFoundString = Args.firstString(props, "not-found-key");

          assertAll(
              "empty not-found-key",
              () -> assertNotNull(notFoundString),
              () -> assertEquals("", notFoundString)
          );
        }
    );
  }

  @Test
  @DisplayName("Args.anyStrings extension test")
  void anyStrings() {

    final String[] args = {
        "--a-string=string-value",
        "--list-of-strings=string-value-1,string-value-2,string-value-3",
    };

    final Map<String, List<String>> props = Args.props(args);

    assertAll(
        "anyStrings",

        () -> {

          final List<String> listOfStrings = Args.anyStrings(props, "list-of-strings");

          assertAll(
              "list-of-strings",
              () -> assertEquals(3, listOfStrings.size()),
              () -> assertEquals("string-value-1", listOfStrings.get(0)),
              () -> assertEquals("string-value-2", listOfStrings.get(1)),
              () -> assertEquals("string-value-3", listOfStrings.get(2))
          );
        },

        () -> {

          final List<String> listOfSingleString = Args.anyStrings(props, "a-string");

          assertAll(
              "a-string",
              () -> assertEquals(1, listOfSingleString.size()),
              () -> assertEquals("string-value", listOfSingleString.get(0))
          );
        },

        () -> {

          final List<String> emptyList = Args.anyStrings(props, "key-not-found");

          assertAll(
              "key-not-found",
              () -> assertNotNull(emptyList),
              () -> assertEquals(0, emptyList.size())
          );
        }
    );
  }

  @Test
  @DisplayName("testing Array<String>.props extension test")
  void props() {

    final String[] args = {
        "--a-string=string-value",
        "--list-of-strings=string-value-1,string-value-2,string-value-3",
        "--an-integer=1",
        "--list-of-integers=-1,0,1,2,3",
        "--a-long=-1234567890",
        "--list-of-longs=-11234567890,1,0,3",
        "--a-boolean=true",
        "--list-of-booleans=true,false,true,true,false,false",
    };

    final Map<String, List<String>> props = Args.props(args);

    assertAll(
        "args",
        () -> {

          assertNotNull(props);

          assertEquals(8, props.size());

          assertAll(
              "sizes of lists",
              () -> assertNull(props.get("key-not-found")),
              () -> assertEquals(1, props.get("a-string").size()),
              () -> assertEquals(3, props.get("list-of-strings").size()),
              () -> assertEquals(1, props.get("an-integer").size()),
              () -> assertEquals(5, props.get("list-of-integers").size()),
              () -> assertEquals(1, props.get("a-long").size()),
              () -> assertEquals(4, props.get("list-of-longs").size()),
              () -> assertEquals(1, props.get("a-boolean").size()),
              () -> assertEquals(6, props.get("list-of-booleans").size())
          );
      }
    );
  }
}
