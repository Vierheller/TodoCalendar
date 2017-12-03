package de.vierheller.todocalendar.view

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import de.vierheller.todocalendar.R
import de.vierheller.todocalendar.model.todo.Task
import kotlinx.android.synthetic.main.fragment_list.*
import org.jetbrains.anko.find


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

    lateinit var adaper:RecyclerTaskListAdapter;

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

        recyclerList.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        recyclerList.layoutManager = layoutManager


        adaper = RecyclerTaskListAdapter(null);
        recyclerList.adapter = adaper;

        (activity as MainActivity).todoViewModel!!.getTasks()
                .observe(this, Observer<List<Task>> { tasks ->
                    Log.d("ListVierwFragment", "${tasks?.size.toString()} available ")
                    adaper.items = tasks
                    adaper.notifyDataSetChanged()
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

        fun newInstance(): ListViewFragment {
           return ListViewFragment()
        }
    }
}

class RecyclerTaskListAdapter (var items:List<Task>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val masterView:View;

        masterView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.list_item_task, parent, false);

        val title = masterView.find<TextView>(R.id.item_list_task_title);

        return TaskViewHolder(masterView, title);
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val task: Task = items!![position];

        val cur_holder = holder as TaskViewHolder;
        cur_holder.title.text = task.taskName;
    }

    override fun getItemCount(): Int {
        Log.d("View holder ","ViewHolder size ${items?.size.toString()}")
        return items?.size ?: 0;
    }


}

class TaskViewHolder(itemView: View?, val title:TextView) : RecyclerView.ViewHolder(itemView) {

}