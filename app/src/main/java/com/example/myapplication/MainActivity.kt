package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonArrayRequest
import org.xmlpull.v1.XmlPullParserFactory


//Leitura de APIs
private lateinit var requestQueue: RequestQueue
//transformação de JSON em XML
private var xmlFactoryObject: XmlPullParserFactory? = XmlPullParserFactory.newInstance()
private val myparser = xmlFactoryObject!!.newPullParser()

class MainActivity : AppCompatActivity() {
    private lateinit var addButton: Button
    private lateinit var dataTextView: TextView

    private val dataList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialização dos componentes de interface
        addButton = findViewById(R.id.addButton)
        dataTextView = findViewById(R.id.dataTextView)

        // Configuração do listener para o botão "Adicionar"
        addButton.setOnClickListener {
            // Cria um Intent para abrir a AddDataActivity
            val intent = Intent(this, AddDataActivity::class.java)
            // Inicia a activity esperando um resultado
            startActivityForResult(intent, ADD_DATA_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Verifica se o resultado é da activity AddDataActivity
        if (requestCode == ADD_DATA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Obtém o nome adicionado da Intent
            val name = data?.getStringExtra("name")
            name?.let {
                // Adiciona o termo buscado à lista de dados
                dataList.add("Termo buscado: "+it)
                //Coloca o texto entrado no terminal
                System.out.println(it)

                //Início da config para puxar dados da API - Usa o volley
                val appnetwork = BasicNetwork(HurlStack())
                val appcache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap
                requestQueue = RequestQueue(appcache, appnetwork).apply {
                    start()
                }
                //URL de busca do dicionário
                var url:String="https://api.dicionario-aberto.net/word/$it"
                val jsonObjectRequest = JsonArrayRequest(
                    Request.Method.GET, url, null,
                    { response ->
                        //conseguiu buscar termo no dicionário
                        System.out.println("Deu certo")
                        val jsonObj=response.getJSONObject(0)
                        //mostra saida no terminal
                        System.out.println(jsonObj.get("xml").toString())
                        //pega o item do retorno da API desejado
                        var saida=jsonObj.get("xml").toString()
                        //localiza dentro do XML a parte da definição da palavra
                        saida = saida.substring(saida.indexOf("<def>") + 5)
                        saida = saida.substring(0, saida.indexOf("</def>"))
                        //adciona a definição na saída
                        dataList.add("Definição: "+saida)
                        updateDataTextView()
                        dataList.add("Fonte: "+url)
                        updateDataTextView()
                    },
                    { error ->
                        //em caso de erro informa no LOG e no resultado da tela
                        Log.d("vol",error.toString())
                        dataList.add("Erro na leitura do API do dicionário")
                        updateDataTextView()
                        dataList.add(url+" "+error.toString())
                        updateDataTextView()
                    }
                )
                //cria o objeto de busca via API
                requestQueue.add(jsonObjectRequest)
            }
        }
    }

    private fun updateDataTextView() {
        // Junta os dados em uma única string separada por quebras de linha
        val dataText = dataList.joinToString("\n")
        // Atualiza o texto do TextView com os dados
        dataTextView.text = dataText
    }

    companion object {
        private const val ADD_DATA_REQUEST_CODE = 1
    }
}