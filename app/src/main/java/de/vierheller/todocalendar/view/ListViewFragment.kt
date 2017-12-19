package de.vierheller.todocalendar.view

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.vierheller.todocalendar.R
import de.vierheller.todocalendar.model.todo.Task
import de.vierheller.todocalendar.view.extra.SimpleDividerItemDecoration
import kotlinx.android.synthetic.main.fragment_list.*
import org.jetbrains.anko.find
import java.text.DateFormat
import java.util.*
import android.support.v7.widget.helper.ItemTouchHelper
import de.vierheller.todocalendar.view.extra.RecyclerItemTouchHelper


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ListViewFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ListViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListViewFragment : Fragment() {
    private var mListener: OnFragmentInteractionListener? = null
    lateinit var mActivity:MainActivity
    lateinit var adapter:RecyclerTaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActivity = activity as MainActivity

        recyclerList.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.isAutoMeasureEnabled = false
        recyclerList.layoutManager = layoutManager
        recyclerList.addItemDecoration(SimpleDividerItemDecoration(activity))
        recyclerList.itemAnimator = DefaultItemAnimator()


        val itemTouchHelperCallback = RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT){ viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int ->
            Snackbar.make(getView()!!, "Deleted!", Snackbar.LENGTH_LONG).show()
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerList)


        adapter = RecyclerTaskListAdapter(null)
        adapter.setOnClickListener { v ->
            val pos = recyclerList.getChildLayoutPosition(v)
            val id = adapter.getItemId(pos)
            Log.d("Tag", "Clicked Task id:"+id)
            mActivity.startTaskActivity(id)
        }
        recyclerList.adapter = adapter


        (activity as MainActivity).todoViewModel.getTasks()
                .observe(this, Observer<List<Task>> { tasks ->
                    Log.d("ListViewFragment", "${tasks?.size.toString()} available ")
                    adapter.items = tasks
                    adapter.notifyDataSetChanged()
            })

    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {

    }

    companion object {
        val dateFormat:DateFormat = DateFormat.getDateInstance()
        val timeFormat:DateFormat = DateFormat.getTimeInstance()

        fun newInstance(): ListViewFragment {

           return ListViewFragment()
        }
    }
}

class RecyclerTaskListAdapter (var items:List<Task>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listener: ((View) -> Unit)? = null

    fun setOnClickListener(listener:(View)->Unit){
        this.listener = listener
    }

    fun removeOnClicklistener(){
        this.listener = null
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val masterView:View

        masterView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.list_item_task, parent, false)



        val title   = masterView.find<TextView>(R.id.item_list_task_title)
        val date    = masterView.find<TextView>(R.id.item_list_task_time_start)
        val time    = masterView.find<TextView>(R.id.item_list_task_time_hour)
        val viewForeground = masterView.find<View>(R.id.list_item_foreground)
        val viewBackground = masterView.find<View>(R.id.list_item_background)

        viewForeground.setOnClickListener{
            listener?.invoke(masterView)
        }

        return TaskViewHolder(masterView, title, date, time, viewForeground, viewBackground)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val task: Task = items!![position]

        val curHolder = holder as TaskViewHolder
        curHolder.title.text = task.taskName
        curHolder.date.text = ListViewFragment.dateFormat.format(Date(task.startDate))
        curHolder.time.text = ListViewFragment.timeFormat.format(Date(task.startDate))
    }

    override fun getItemId(position: Int): Long {
        return items?.get(position)?.uid!!
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }


}

class TaskViewHolder(itemView: View?, val title: TextView, val date: TextView, val time:TextView, val viewForeground:View, val viewBackground:View) : RecyclerView.ViewHolder(itemView)