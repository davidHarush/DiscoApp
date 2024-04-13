package com.harush.david.discoapp.ui.data_model


/**
 * Data class representing a feed item in the UI layer.
 * This class is used to separate between the network layer and the UI layer.
 */
data class FeedItem (
    val title: String,
    val description: String,
    val link: String
) {
    override fun toString(): String {
        return "FeedItem(title='$title', description='$description', link='$link')"
    }
}