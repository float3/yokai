package yokai.domain.manga

import eu.kanade.tachiyomi.data.database.models.LibraryManga
import eu.kanade.tachiyomi.data.database.models.Manga
import kotlinx.coroutines.flow.Flow
import yokai.domain.manga.models.MangaUpdate

interface MangaRepository {
    suspend fun getManga(): List<Manga>
    fun getMangaAsFlow(): Flow<List<Manga>>
    suspend fun getLibraryManga(): List<LibraryManga>
    fun getLibraryMangaAsFlow(): Flow<List<LibraryManga>>
    suspend fun update(update: MangaUpdate): Boolean
    suspend fun updateAll(updates: List<MangaUpdate>): Boolean
    suspend fun insert(manga: Manga): Long?
}
