package com.adammcneilly.pokedex.models

import com.adammcneilly.pokedex.database.models.PersistablePokemon
import com.adammcneilly.pokedex.database.models.PersistableSprites
import com.adammcneilly.pokedex.database.models.PersistableType
import com.adammcneilly.pokedex.database.models.PersistableTypeSlot
import com.adammcneilly.pokedex.network.models.PokemonDTO
import com.adammcneilly.pokedex.network.models.SpritesDTO
import com.adammcneilly.pokedex.network.models.TypeDTO
import com.adammcneilly.pokedex.network.models.TypeSlotDTO
import org.junit.Assert.assertEquals
import org.junit.Test

class PokemonTest {
    @Test
    fun mapToPersistablePokemon() {
        val testName = "Test Name"
        val testSprites = Sprites("Test")
        val testUrl = "Test URL"
        val testTypes = listOf(TypeSlot(1, Type("Test")))
        val pokemon = Pokemon(
            name = testName,
            sprites = testSprites,
            url = testUrl,
            types = testTypes
        )

        val persistablePokemon = pokemon.toPersistablePokemon()
        assertEquals(testName, persistablePokemon.name)
        assertEquals(testSprites.toPersistableSprites(), persistablePokemon.sprites)
        assertEquals(testUrl, persistablePokemon.url)
        assertEquals(testTypes.map(TypeSlot::toPersistableTypeSlot), persistablePokemon.types)
    }

    @Test
    fun mapFromPersistablePokemon() {
        val testName = "Test Name"
        val testSprites = PersistableSprites("Test")
        val testUrl = "Test URL"
        val testTypes = listOf(PersistableTypeSlot(1, PersistableType("Test")))
        val persistablePokemon = PersistablePokemon(
            name = testName,
            sprites = testSprites,
            url = testUrl,
            types = testTypes
        )

        val pokemon = persistablePokemon.toPokemon()
        assertEquals(testName, pokemon?.name)
        assertEquals(testSprites.toSprites(), pokemon?.sprites)
        assertEquals(testUrl, pokemon?.url)
        assertEquals(testTypes.map(PersistableTypeSlot::toTypeSlot), pokemon?.types)
    }

    @Test
    fun sortTypes() {
        val secondTypeSlot = TypeSlot(2, Type("Second"))
        val firstTypeSlot = TypeSlot(1, Type("First"))
        val disorderedTypes = listOf(secondTypeSlot, firstTypeSlot)

        val pokemon = Pokemon(types = disorderedTypes)
        val sortedTypes = pokemon.sortedTypes
        val expectedTypes = listOf(firstTypeSlot.type, secondTypeSlot.type)
        assertEquals(expectedTypes, sortedTypes)
    }

    @Test
    fun mapToPokemonDTO() {
        val testName = "Test Name"
        val testSprites = Sprites("Test")
        val testUrl = "Test URL"
        val testTypes = listOf(TypeSlot(1, Type("Test")))
        val pokemon = Pokemon(
            name = testName,
            sprites = testSprites,
            url = testUrl,
            types = testTypes
        )

        val pokemonDTO = pokemon.toPokemonDTO()
        assertEquals(testName, pokemonDTO.name)
        assertEquals(testSprites.toSpritesDTO(), pokemonDTO.sprites)
        assertEquals(testUrl, pokemonDTO.url)
        assertEquals(testTypes.map(TypeSlot::toTypeSlotDTO), pokemonDTO.types)
    }

    @Test
    fun mapFromPokemonDTO() {
        val testName = "Test Name"
        val testSprites = SpritesDTO("Test")
        val testUrl = "Test URL"
        val testTypes = listOf(TypeSlotDTO(1, TypeDTO("Test")))
        val pokemonDTO = PokemonDTO(
            name = testName,
            sprites = testSprites,
            url = testUrl,
            types = testTypes
        )

        val pokemon = pokemonDTO.toPokemon()
        assertEquals(testName, pokemon?.name)
        assertEquals(testSprites.toSprites(), pokemon?.sprites)
        assertEquals(testUrl, pokemon?.url)
        assertEquals(testTypes.map(TypeSlotDTO::toTypeSlot), pokemon?.types)
    }
}