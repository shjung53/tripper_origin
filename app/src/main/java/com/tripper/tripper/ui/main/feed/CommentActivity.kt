package com.tripper.tripper.ui.main.feed
import CommentListView
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tripper.tripper.LoadingDialog
import com.tripper.tripper.R
import com.tripper.tripper.databinding.ActivityCommentBinding
import com.tripper.tripper.getJwt
import com.tripper.tripper.getUserIdx
import com.tripper.tripper.ui.main.feed.comment.CommentIdx
import com.tripper.tripper.ui.main.feed.comment.CommentService
import com.tripper.tripper.ui.main.feed.comment.CommentView
import com.tripper.tripper.ui.main.feed.commentEdit.Comment
import com.tripper.tripper.ui.main.feed.commentEdit.CommentEditService
import com.tripper.tripper.ui.main.feed.commentEdit.CommentEditView
import com.tripper.tripper.ui.main.feed.deleteComment.DeleteCommentService
import com.tripper.tripper.ui.main.feed.deleteComment.DeleteCommentView
import com.tripper.tripper.ui.main.feed.reply.Reply
import com.tripper.tripper.ui.main.feed.reply.ReplyService
import com.tripper.tripper.ui.main.feed.reply.ReplyView
import com.tripper.tripper.ui.main.feed.searchComment.CommentInfo
import com.tripper.tripper.ui.main.feed.searchComment.CommentListService
import com.tripper.tripper.ui.main.feed.searchComment.Comments
import gun0912.tedimagepicker.util.ToastUtil.context

class CommentActivity : AppCompatActivity(), CommentView, CommentEditView, CommentListView, DeleteCommentView {//}, ReplyView {
    lateinit var binding: ActivityCommentBinding

    private var comments = ArrayList<Comments>()

//    private var commentsR = ArrayList<CommentsR>()

//    private var reply = ArrayList<Reply>()

    private lateinit var commentService: CommentService

    private lateinit var commentData : com.tripper.tripper.ui.main.feed.comment.CommentData

    private lateinit var commentEditService: CommentEditService

    private lateinit var commentListService: CommentListService

    private lateinit var commentRVAdapter: CommentRVAdapter

    private lateinit var deleteCommentService: DeleteCommentService

//    private lateinit var replyService: ReplyService

    private val commentViewModel: CommentViewModel by viewModels()
    var page = 1
    var savePage = 0
    var feedIdx = 0
    var position = 0
    var replyPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        commentService = CommentService()
        commentService.setCommentView(this)
        commentEditService = CommentEditService()
        commentEditService.setCommentEditView(this)
        commentListService = CommentListService()
        commentListService.setCommentListView(this)
        deleteCommentService = DeleteCommentService()
        deleteCommentService.setDeleteCommentView(this)
//        replyService = ReplyService()
//        replyService.setReplyView(this)


        val jwtToken = getJwt(this)

        feedIdx = intent.getIntExtra("feedIdx",0)

        commentViewModel.getPage(page)
        commentListService.getCommentList(jwtToken,feedIdx,page)

        //        댓글 어댑터
//        commentRVAdapter = CommentRVAdapter(this, commentsR, reply)
        commentRVAdapter = CommentRVAdapter(this, comments)
        binding.commentCommentRv.adapter = commentRVAdapter
        binding.commentCommentRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        // (수정, 삭제) + (신고, 취소) 다이얼로그 관련
        val editDialogView = layoutInflater.inflate(R.layout.dialog_edit, null)
        val reportDialogView = layoutInflater.inflate(R.layout.dialog_report, null)

        val alertEditDialog = AlertDialog.Builder(this, R.style.NewDialog)
            .setView(editDialogView)
            .create()

        val alertReportDialog = AlertDialog.Builder(this, R.style.NewDialog)
            .setView(reportDialogView)
            .create()

        val dialogEditBtn = editDialogView.findViewById<CardView>(R.id.dialog_edit_edit_cv) // (수정, 삭제)에서 수정 버튼
        val dialogDeleteBtn = editDialogView.findViewById<CardView>(R.id.dialog_edit_delete_cv) // (수정, 삭제)에서 삭제 버튼

        val dialogReportBtn = reportDialogView.findViewById<CardView>(R.id.dialog_report_report_cv) // (신고, 취소)에서 신고 버튼
        val dialogCancelBtn = reportDialogView.findViewById<CardView>(R.id.dialog_report_cancel_cv) // (신고, 취소)에서 취소 버튼

        // (수정, 삭제) 다이얼로그 띄우기 => alertEditDialog.show()
        // (수정, 삭제) 다이얼로그 닫기 => alertEditDialog.dismiss()


        val userIdx = getUserIdx(context)

        var commentPosition = -1
        var parentIdx: Int? = null

//        답글달기
        commentRVAdapter.setMyItemClickListener(object : CommentRVAdapter.MyItemClickListener {
//            override fun replyToComment(holder: CommentRVAdapter.ViewHolder, position: Int) {
//                binding.commentReplyIndicatorCv.visibility = View.VISIBLE
//                parentIdx = comments[holder.adapterPosition].commentIdx
//                val parentNickname = comments[holder.adapterPosition].nickName
//                binding.commentReplyIndicatorTv.text = "\'$parentNickname\'님에게 답글"
//            }
//
//            override fun getReply(holder: CommentRVAdapter.ViewHolder) {
//                val idx = comments[holder.adapterPosition].commentIdx
//                replyService.getReply(jwtToken, feedIdx, idx, 1)
//                replyPos = holder.adapterPosition
//                Log.d("대댓글토큰", jwtToken.toString())
//                Log.d("대댓글feedIdx", feedIdx.toString())
//                Log.d("대댓글idx", idx.toString())
//            }

            override fun onCommentThreeDotClick(holder: CommentRVAdapter.ViewHolder) {
                commentPosition = holder.adapterPosition
                commentViewModel.getPosition(commentPosition)
                if (comments[commentPosition].userIdx == userIdx){
                    alertEditDialog.show() // (수정, 삭제) 다이얼로그 띄우기
                } else {
                    alertReportDialog.show() // (신고, 취소) 다이얼로그 띄우기
                }

                dialogEditBtn.setOnClickListener {
                    binding.commentCommentRv.smoothScrollToPosition(comments[commentPosition].commentIdx)
                    alertEditDialog.dismiss()
                    val commentEt = holder.binding.itemCommentCommentEt
                    val commentPostBtn = holder.binding.itemCommentPostTv
                    commentEt.isEnabled = true
                    commentEt.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.main)
                    commentPostBtn.visibility = View.VISIBLE
                    commentPostBtn.setOnClickListener {
                        commentEt.isEnabled = false
                        commentEt.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.transparent)
                        commentPostBtn.visibility = View.GONE
                        val commentIdx = comments[commentPosition].commentIdx
                        val commentText = commentEt.text.toString()
                        val comment = Comment(commentText)
                        commentEditService.commentEdit(jwtToken,feedIdx,commentIdx,comment)
                    }
                }

                dialogDeleteBtn.setOnClickListener {
                    alertEditDialog.dismiss()
                    val commentIdx = comments[commentPosition].commentIdx
                    deleteCommentService.deleteComment(jwtToken, feedIdx, commentIdx)
                }
            }

//            override fun onReplyThreeDotClick(holder: ReplyRVAdapter.ViewHolder) {
//                val replyPosition = holder.adapterPosition
//                if (reply[replyPosition].userIdx == userIdx){
//                    alertEditDialog.show() // (수정, 삭제) 다이얼로그 띄우기
//                } else {
//                    alertReportDialog.show() // (신고, 취소) 다이얼로그 띄우기
//                }
//
//                dialogEditBtn.setOnClickListener {
//                    alertEditDialog.dismiss()
//                    val replyEt = holder.binding.itemReplyReplyEt
//                    val replyPostBtn = holder.binding.itemReplyPostTv
//                    replyEt.isEnabled = true
//                    replyEt.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.main)
//                    replyPostBtn.visibility = View.VISIBLE
//                    replyPostBtn.setOnClickListener {
//                        replyEt.isEnabled = false
//                        replyEt.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.transparent)
//                        replyPostBtn.visibility = View.GONE
//                        val replyIdx = reply[replyPosition].commentIdx
//                        val replyText = replyEt.text.toString()
//                        Log.d("대댓글", replyText)
//                        val comment = Comment(replyText)
//                        commentEditService.commentEdit(jwtToken,feedIdx,replyIdx,comment)
//                    }
//                }
//            }
        })


        dialogCancelBtn.setOnClickListener {
            alertReportDialog.dismiss() // (신고, 취소) 다이얼로그 닫기
        }

        binding.commentIcBackIv.setOnClickListener {
            onBackPressed()
        }

        binding.commentReplyIndicatorCancelIv.setOnClickListener {
            binding.commentReplyIndicatorCv.visibility = View.INVISIBLE
        }

//        댓글 등록
        binding.commentPostTv.setOnClickListener {
            val commentText = binding.commentCommentEt.text.toString()

            if (binding.commentReplyIndicatorCv.visibility == View.INVISIBLE){
                parentIdx = null
            }
            commentData= com.tripper.tripper.ui.main.feed.comment.CommentData(feedIdx,commentText,parentIdx)
            commentService.postComment(jwtToken,commentData)
            binding.commentCommentEt.text = null
        }


        binding.commentCommentRv.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                page = commentViewModel.page.value!!
                comments = commentViewModel.commentData.value!!

                val lastItemPosition = comments.size - 1
                if (lastVisibleItemPosition == lastItemPosition && jwtToken != null) {
                    if (savePage == page) {
                        Toast.makeText(context, "더 이상 댓글이 없습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        commentListService.getCommentList(jwtToken, feedIdx, page)
                        savePage = page
                    }
                }


            }
        })



    }

    override fun onCommentSuccess(code: Int, message: String, result: CommentIdx) {
        LoadingDialog.loadingOff()
        comments.clear()
//        commentRVAdapter.resetComment(commentsR)
        commentRVAdapter.resetComment(comments)
//        reply.clear()
        page = 1
        savePage = 0
        commentViewModel.getPage(1)
        val jwtToken = getJwt(this)
        commentListService.getCommentList(jwtToken, feedIdx,page)
    }

    override fun onCommentFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCommentLoading() {
        LoadingDialog.loadingOn(this)
    }

    override fun onCommentEditSuccess(message: String) {
        LoadingDialog.loadingOff()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCommentEditFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCommentEditLoading() {
        LoadingDialog.loadingOn(this)
    }


    override fun onCommentListSuccess(message: String, result: CommentInfo) {
        LoadingDialog.loadingOff()
        val size = result.comments.size
        for(i in 1 .. size){
//            commentsR.apply { add(CommentsR(result.comments[i-1].commentIdx, result.comments[i-1].userIdx,
//                result.comments[i-1].nickName, result.comments[i-1].profileImgUrl, result.comments[i-1].comment,
//                result.comments[i-1].commentStatus, result.comments[i-1].secondCommentCount, result.comments[i-1].createdAt,
//                reply
//            ))}
            comments.apply { add(result.comments[i-1])}
        }

//        commentRVAdapter.setComment(commentsR)
        commentRVAdapter.setComment(comments)
        commentViewModel.getCommentData(comments)
        commentViewModel.getPage(commentViewModel.page.value!! + 1)

        binding.commentNumTv.text = result.totalCommentCount.toString()
    }

    override fun onCommentListFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
    }

    override fun onCommentListLoading() {
        LoadingDialog.loadingOn(this)
    }

    override fun onDeleteCommentSuccess(message: String) {
        LoadingDialog.loadingOff()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        position = commentViewModel.position.value!!
        comments = commentViewModel.commentData.value!!
        comments.removeAt(position)
        commentViewModel.getCommentData(comments)
//        commentRVAdapter.removeComment(commentsR, position)
        commentRVAdapter.removeComment(comments, position)
        binding.commentCommentRv.itemAnimator = null
    }

    override fun onDeleteCommentFailure(code: Int, message: String) {
        LoadingDialog.loadingOff()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteCommentLoading() {
        LoadingDialog.loadingOn(this)
    }

//    override fun onReplyLoading() {
//        LoadingDialog.loadingOn(this)
//    }
//
//    override fun onReplySuccess(code: Int, message: String, result: ArrayList<Reply>) {
//        LoadingDialog.loadingOff()
//        commentRVAdapter.setReply(result, replyPos)
//    }
//
//    override fun onReplyFailure(code: Int, message: String) {
//        LoadingDialog.loadingOff()
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
}

//data class CommentsR(
//    var commentIdx: Int,
//    var userIdx: Int,
//    var nickName: String,
//    var profileImgUrl: String,
//    var comment: String,
//    var commentStatus: String,
//    var secondCommentCount: Int?,
//    var createdAt: String,
//    var allReplies : ArrayList<Reply>?
//    )
