package de.dannyb.moviesapp.movies.paging

import de.dannyb.moviesapp.common.MoviesGenericPagingDataSource
import de.dannyb.moviesapp.data.DiscoverMovieModel
import de.dannyb.moviesapp.networking.MoviesDbService

class MoviesPagingDataSource(
    moviesDbService: MoviesDbService
) : MoviesGenericPagingDataSource<Int, DiscoverMovieModel>(moviesDbService) {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DiscoverMovieModel> {
        val page = params.key ?: 1

        return try {
            val moviesResponse = moviesDbService.discover(page)

            val prevKey = getPreviousPageNumber(moviesResponse.page)
            val nextKey = getNextPageNumber(moviesResponse.page, moviesResponse.totalPages)

            LoadResult.Page(
                data = moviesResponse.results,
                prevKey = prevKey,
                nextKey = nextKey,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
