package de.vierheller.todocalendar.model.project

/**
 * Created by Vierheller on 02.01.2018.
 */
class Tree <T>{
    private var root:Node<T>

    constructor(){
        this.root = Node(null, null, mutableListOf())
    }

    fun getRoot(): Node<T> {
        return this.root
    }

    override fun toString(): String {
        return this.root.toString()
    }

    class Node <T>(var data:T?, var parent:Node<T>?, var children:MutableList<Node<T>>){
        override fun toString(): String {
            val builder = StringBuilder()
            for (i in 0 until children.size){
                val child = children[i]
                if(i!=0)
                    builder.append(", ")
                builder.append(child.toString())
            }
            return "${data.toString()} : children = { $builder }"
        }
    }
}