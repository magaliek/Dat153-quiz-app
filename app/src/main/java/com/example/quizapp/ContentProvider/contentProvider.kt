package com.example.quizapp.ContentProvider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.example.quizapp.MemeData
import com.example.quizapp.data.MemeDao


class MemeContentProvider : ContentProvider() {
    private lateinit var memeDao : MemeDao
    override fun onCreate(): Boolean {
        val data = context!!.applicationContext as MemeData
        memeDao = data.memeDao
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String?>?,
        selection: String?,
        selectionArgs: Array<out String?>?,
        sortOrder: String?
    ): Cursor {
        val memes = memeDao.getAllSync()
        val cursor = MatrixCursor(arrayOf("name", "URI"))
        memes.forEach { meme ->
            cursor.addRow(arrayOf(meme.label, meme.uri))
        }
        return cursor
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String?>?
    ): Int {
        throw UnsupportedOperationException("Not supported")
    }

    override fun getType(uri: Uri): String {
        throw UnsupportedOperationException("Not supported")
    }

    override fun insert(
        uri: Uri,
        values: ContentValues?
    ): Uri {
        throw UnsupportedOperationException("Not supported")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String?>?
    ): Int {
        throw UnsupportedOperationException("Not supported")
    }

}