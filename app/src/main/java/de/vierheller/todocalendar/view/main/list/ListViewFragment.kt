package de.vierheller.todocalendar.view.main.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import de.vierheller.todocalendar.R
import de.vierheller.todocalendar.model.todo.Task
import de.vierheller.todocalendar.view.extra.RecyclerItemTouchHelper
import de.vierheller.todocalendar.view.extra.SimpleDividerItemDecoration
import de.vierheller.todocalendar.view.main.MainActivity
import de.vierheller.todocalendar.viewmodel.ListViewFragmentViewModel
import kotlinx.android.synthetic.main.fragment_list.*
import java.text.DateFormat


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
    lateinit var mActivity: MainActivity
    lateinit var adapter: RecyclerTaskListAdapter
    lateinit var viewModel:ListViewFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(activity).get(ListViewFragmentViewModel::class.java)
        viewModel.init(mActivity.todoViewModel)

        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_list, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.fragment_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return viewModel.menuItemSelected(item!!.itemId)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerList.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.isAutoMeasureEnabled = false
        recyclerList.layoutManager = layoutManager
        recyclerList.addItemDecoration(SimpleDividerItemDecoration(activity))
        recyclerList.itemAnimator = DefaultItemAnimator()


        val itemTouchHelperCallback = RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT){ viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int ->
            if(viewModel.finishTask(position)){
                Snackbar.make(getView()!!, R.string.task_item_check, Snackbar.LENGTH_LONG).show()
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerList)


        adapter = RecyclerTaskListAdapter(null)
        adapter.setOnClickListener { v ->
            val pos = recyclerList.getChildLayoutPosition(v)
            val id = adapter.getItemId(pos)
            mActivity.startTaskActivity(id)
        }
        recyclerList.adapter = adapter


        viewModel.getTasks()
                .observe(this, Observer<List<Task>> { tasks ->
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

