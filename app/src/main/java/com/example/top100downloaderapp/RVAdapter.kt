package com.example.top100downloaderapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import android.graphics.Color
import android.net.Uri
import com.bumptech.glide.Glide
import com.example.top100downloaderapp.databinding.ItemRowBinding


class RVAdapter(private val feeds: List<Feeds>,val context: Context): RecyclerView.Adapter<RVAdapter.ItemViewHolder>() {
    class ItemViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val title = feeds[position].title
        val image = feeds[position].image
        val link = feeds[position].link

        holder.binding.apply {
            tvTitle.text=title

            Glide.with(context)
               .load(Uri.parse(image))
               .into(appIcone)

            holder.itemView.setOnClickListener{
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + tvTitle.text))
                context.startActivity(browserIntent)
            }

        }

    }

    override fun getItemCount() = feeds.size

    fun customAlert(title:String, detail:String )
    {
        var msg= TextView(context)

        //to show in html format
        msg.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(detail, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(detail)
        }
        msg.setTextColor(Color.parseColor("#465DE2"));

        // first we create a variable to hold an AlertDialog builder
        val dialogBuilder = android.app.AlertDialog.Builder(context)
        // here we set the message of our alert dialog
        //dialogBuilder.setMessage(detail)
            // negative button text and action
            .setNegativeButton("Ok", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle(title)
        alert.setView(msg)
        // show alert dialog
        alert.show()
    }
}