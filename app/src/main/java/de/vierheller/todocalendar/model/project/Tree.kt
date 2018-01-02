package de.vierheller.todocalendar.model.project

/**
 * Created by Vierheller on 02.01.2018.
 */
class Tree <T>{
    private lateinit var root:Node<T>

    constructor(){
        this.root = Node(null, null, mutableListOf())
    }

    fun getRoot(): Node<T> {
        return this.root
    }

    class Node <T>(var data:T?, var parent:Node<T>?, var children:MutableList<Node<T>>)
}