import com.tripper.tripper.ui.main.feed.searchComment.CommentInfo

interface CommentListView{
    fun onCommentListSuccess(message: String, result: CommentInfo)
    fun onCommentListFailure(code: Int, message: String)
    fun onCommentListLoading()
}