/*
 * "Bolsillos" (c) by R8V.
 * "Bolsillos" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 */

package cl.uchile.dcc.poke

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
import io.kotest.property.assume
import io.kotest.property.checkAll

class WaterPokemonSpec : FunSpec({

    test("Two Pokémon with the same parameters are equal") {
        checkAll(Arb.string(), Arb.positiveInt(1000), Arb.positiveInt(100000)) { name, hp, str ->
            val pokemon1 = WaterPokemon(name, hp, str)
            val pokemon2 = WaterPokemon(name, hp, str)
            pokemon1 shouldNotBeSameInstanceAs pokemon2
            pokemon1 shouldBe pokemon2
        }
    }

    test("Two Pokémon with different names are not equal") {
        checkAll(
            Arb.string(),
            Arb.string(),
            Arb.positiveInt(1000),
            Arb.positiveInt(100000),
        ) { name1, name2, hp, str ->
            assume(name1 != name2)
            val pokemon1 = WaterPokemon(name1, hp, str)
            val pokemon2 = WaterPokemon(name2, hp, str)
            pokemon1 shouldNotBe pokemon2
        }
    }

    test("A Pokémon can attack another Pokémon") {
        checkAll(
            Arb.string(),
            Arb.string(),
            Arb.positiveInt(1000),
            Arb.positiveInt(1000),
            Arb.positiveInt(1000),
            Arb.positiveInt(1000)
        ) { name1, name2, hp1, hp2, str1, str2 ->
            assume(hp2 - str1 / 10 > 0)
            val pokemon1 = WaterPokemon(name1, hp1, str1)
            val pokemon2 = WaterPokemon(name2, hp2, str2)
            pokemon1.attack(pokemon2)
            pokemon2.currentHp shouldBe (hp2 - str1 / 10)
        }
    }

    test("A Pokémon can be KO") {
        checkAll(Arb.string(), Arb.positiveInt(), Arb.positiveInt()) { name, hp, str ->
            val pokemon = WaterPokemon(name, hp, str)
            pokemon.isKo() shouldBe false
            pokemon.currentHp = 0
            pokemon.isKo() shouldBe true
        }
    }

    test("A Pokémon can be KOd by an attack") {
        checkAll(
            Arb.string(),
            Arb.string(),
            Arb.positiveInt(1000),
            Arb.positiveInt(1000),
            Arb.positiveInt(100000),
            Arb.positiveInt(100000)
        ) { name1, name2, hp1, hp2, str1, str2 ->
            assume(hp2 - str1 / 10 <= 0)
            val pokemon1 = WaterPokemon(name1, hp1, str1)
            val pokemon2 = WaterPokemon(name2, hp2, str2)
            pokemon1.attack(pokemon2)
            pokemon2.currentHp shouldBe 0
            pokemon2.isKo() shouldBe true
        }
    }
})