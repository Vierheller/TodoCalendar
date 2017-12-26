package de.vierheller.todocalendar.view.main.list.extra

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView
import android.util.SparseArray
import android.view.View


/**
 * Created by Vierheller on 26.12.2017.
 * Copied from https://gist.github.com/gabrielemariotti/4c189fb1124df4556058
 */
class SimpleSectionedRecyclerViewAdapter(private val mContext: Context, private val mSectionResourceId: Int, private val mTextResourceId: Int,
                                         private val mBaseAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mValid = true
    private val mLayoutInflater: LayoutInflater
    private val mSections = SparseArray<Section>()


    init {

        mLayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        mBaseAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                mValid = mBaseAdapter.itemCount > 0
                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                mValid = mBaseAdapter.itemCount > 0
                notifyItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                mValid = mBaseAdapter.itemCount > 0
                notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                mValid = mBaseAdapter.itemCount > 0
                notifyItemRangeRemoved(positionStart, itemCount)
            }
        })
    }


    class SectionViewHolder(view: View, mTextResourceid: Int) : RecyclerView.ViewHolder(view) {

        var title: TextView

        init {
            title = view.findViewById(mTextResourceid)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, typeView: Int): RecyclerView.ViewHolder {
        if (typeView == SECTION_TYPE) {
            val view = LayoutInflater.from(mContext).inflate(mSectionResourceId, parent, false)
            return SectionViewHolder(view, mTextResourceId)
        } else {
            return mBaseAdapter.onCreateViewHolder(parent, typeView - 1)
        }
    }

    override fun onBindViewHolder(sectionViewHolder: RecyclerView.ViewHolder, position: Int) {
        if (isSectionHeaderPosition(position)) {
            (sectionViewHolder as SectionViewHolder).title.text = mSections.get(position).title
        } else {
            mBaseAdapter.onBindViewHolder(sectionViewHolder, sectionedPositionToPosition(position))
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (isSectionHeaderPosition(position))
            SECTION_TYPE
        else
            mBaseAdapter.getItemViewType(sectionedPositionToPosition(position)) + 1
    }


    class Section(internal var firstPosition: Int, title: CharSequence) {
        internal var sectionedPosition: Int = 0
        var title: CharSequence
            internal set

        init {
            this.title = title
        }
    }


    fun setSections(nSections: List<Section>) {
        mSections.clear()

        var sections = nSections.sortedBy { it.firstPosition }

        var offset = 0 // offset positions for the headers we're adding
        for (section in sections) {
            section.sectionedPosition = section.firstPosition + offset
            mSections.append(section.sectionedPosition, section)
            ++offset
        }

        notifyDataSetChanged()
    }

    fun positionToSectionedPosition(position: Int): Int {
        var offset = 0
        for (i in 0 until mSections.size()) {
            if (mSections.valueAt(i).firstPosition > position) {
                break
            }
            ++offset
        }
        return position + offset
    }

    fun sectionedPositionToPosition(sectionedPosition: Int): Int {
        if (isSectionHeaderPosition(sectionedPosition)) {
            return RecyclerView.NO_POSITION
        }

        var offset = 0
        for (i in 0 until mSections.size()) {
            if (mSections.valueAt(i).sectionedPosition > sectionedPosition) {
                break
            }
            --offset
        }
        return sectionedPosition + offset
    }

    fun isSectionHeaderPosition(position: Int): Boolean {
        return mSections.get(position) != null
    }


    override fun getItemId(position: Int): Long {
        return if (isSectionHeaderPosition(position))
            (Integer.MAX_VALUE - mSections.indexOfKey(position)).toLong()
        else
            mBaseAdapter.getItemId(sectionedPositionToPosition(position))
    }

    override fun getItemCount(): Int {
        return if (mValid) mBaseAdapter.itemCount + mSections.size() else 0
    }

    companion object {
        private val SECTION_TYPE = 0
    }

}