package com.proyecto.movil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.proyect.movil.LoginActivity;
import com.proyect.movil.R;
import com.proyecto.bean.EmpresaBean;
import com.proyecto.cobranza.ListaCobranzasMain;
import com.proyecto.conectividad.Connectivity;
import com.proyecto.facturas.ListaFacturasMain;
import com.proyecto.inventario.ListaArticulosMain;
import com.proyecto.preferences.SettingsMain;
import com.proyecto.reportes.ReporteFragment;
import com.proyecto.servicios.ServicioOvPr;
import com.proyecto.servicios.ServicioSocios;
import com.proyecto.servicios.SincManualTaskDocumentos;
import com.proyecto.servicios.SincManualTaskInicio;
import com.proyecto.servicios.SincManualTaskMaestros;
import com.proyecto.servicios.SyncRestInicio;
import com.proyecto.servicios.SyncRestMaestros;
import com.proyecto.sociosnegocio.ListaSocioNegocio;
import com.proyecto.utils.Variables;
import com.proyecto.ventas.ListaVentasMain;
import com.proyecto.ws.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MainActivityDrawer extends AppCompatActivity {
	
	public static boolean shouldThreadTaskContinue = false;
	public static String action = "";
	
	private DrawerLayout drawerLayout;
	private String drawerTitle;
	private NavigationView navigationView;
	private Context contexto;
	private SharedPreferences pref;
	public static ProgressDialog pd = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_drawer_layout);
		
		contexto = this;
		
		setToolbar(); // Setear Toolbar como action bar

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		navigationView = (NavigationView) findViewById(R.id.nav_view);
		if (navigationView != null) {
			setupDrawerContent(navigationView);
		}

		//navigationView.getMenu().getItem(2).setVisible(false);

		View headerView = navigationView.inflateHeaderView(R.layout.nav_header); //Header del drawer
		TextView txtUserName =  (TextView) headerView.findViewById(R.id.username);
		TextView txtCodigo =  (TextView) headerView.findViewById(R.id.codigo);
		
		//LLenar head del drawer
		pref = PreferenceManager
				.getDefaultSharedPreferences(contexto);
		
		String nombreEmpleado = pref.getString(Variables.NOMBRE_EMPLEADO, "");
		txtUserName.setText(nombreEmpleado);
		
		String codigoEmpleado = pref.getString(Variables.USUARIO_EMPLEADO, "");
		txtCodigo.setText(codigoEmpleado);

		String sociedad = pref.getString("sociedades", "-1");
		String perfil = pref.getString(Variables.PERFIL, "-1");
		String ip = pref.getString("ipServidor", "200.10.84.66");
		String port = pref.getString("puertoServidor", "80");

		drawerTitle = getResources().getString(R.string.titMenuPrincipal);
		if (savedInstanceState == null) {
			selectItem(drawerTitle);
		}

		// VERIFICAR EL ESTADO DE LA CONEXION DEL MOVIL
		boolean wifi = Connectivity.isConnectedWifi(contexto);
		boolean movil = Connectivity.isConnectedMobile(contexto);

		if (wifi || movil) {
			validarOpcionesMenu(sociedad, perfil, ip, port);
		}else{
			validarOpcionesMenuOffLine();
		}
	}


	private void validarOpcionesMenu(String sociedad, String perfil, String ip, String puerto){
		try {

			String ws_ruta = ("http://" + ip + ":" + puerto +
					"/MSS_MOBILE/service/menu/getMenu.xsjs?empId=" + Integer.parseInt(sociedad) +
					"&prfId=" + perfil);

			StringRequest stringRequest = new StringRequest(Request.Method.GET, ws_ruta,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							try {
								JSONObject jsonObject = new JSONObject(response);
								if(jsonObject.getString("ResponseStatus").equals("Success")){
									JSONArray jsonArray = jsonObject.getJSONObject("Response")
																	.getJSONObject("message")
																	.getJSONArray("value");

									int size = jsonArray.length();
									boolean menuVisible = true;

									setMenuVisible(false);

									for (int i = 0; i < size; i++ ){


										JSONObject jsonObj = jsonArray.getJSONObject(i);
										menuVisible = jsonObj.getString("Accesa").equals("Y") ? true: false;

										if(jsonObj.getString("Descripcion").contains(Variables.MENU_SOCIOS_NEGOCIO)){
											navigationView.getMenu().findItem(R.id.nav_socios).setVisible(menuVisible);
										}else if(jsonObj.getString("Descripcion").contains(Variables.MENU_INVENTARIO)){
											navigationView.getMenu().findItem(R.id.nav_inventario).setVisible(menuVisible);
										}else if(jsonObj.getString("Descripcion").contains(Variables.MENU_PEDIDOS)){
											navigationView.getMenu().findItem(R.id.nav_ordenes).setVisible(menuVisible);;
										}else if(jsonObj.getString("Descripcion").contains(Variables.MENU_FACTURAS)){
											navigationView.getMenu().findItem(R.id.nav_facturas).setVisible(menuVisible);
										}else if(jsonObj.getString("Descripcion").contains(Variables.MENU_COBRANZAS)){
											navigationView.getMenu().findItem(R.id.nav_cobranzas).setVisible(menuVisible);
										}else if(jsonObj.getString("Descripcion").contains(Variables.MENU_REPORTES)){
											navigationView.getMenu().findItem(R.id.nav_reportes).setVisible(menuVisible);
										}else if(jsonObj.getString("Descripcion").contains(Variables.MENU_ACTIVIDADES)){
											navigationView.getMenu().findItem(R.id.nav_actividades).setVisible(menuVisible);
										}

										SharedPreferences.Editor editor = pref.edit();

										JSONObject permisos = new JSONObject();
										permisos.put(Variables.MOVIL_ACCESA, jsonObj.getString("Accesa"));
										permisos.put(Variables.MOVIL_CREAR, jsonObj.getString("Crea"));
										permisos.put(Variables.MOVIL_EDITAR, jsonObj.getString("Edita"));
										permisos.put(Variables.MOVIL_APROBAR, jsonObj.getString("Aprueba"));
										permisos.put(Variables.MOVIL_RECHAZAR, jsonObj.getString("Rechaza"));
										permisos.put(Variables.MOVIL_ESCOGER_PRECIO, jsonObj.getString("SelListaPrecio"));

										editor.putString(jsonObj.getString("Descripcion"), permisos.toString());
										editor.commit();
									}
								}else{
									showToast("No se han configurado los accesos para su perfil. Contacte con el administrador.");
									setMenuVisible(false);
								}
							} catch (Exception e){
								showToast(e.getMessage());
							}
						}
					}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					showToast("Ocurri� un error intentando conectar con el servidor " + error.getMessage());
				}
			});

			VolleySingleton.getInstance(contexto).addToRequestQueue(stringRequest);
		}catch (Exception e){
			Toast.makeText(contexto, "Excepci�n no controlada validarOpcionesMenu() > " + e.getMessage(), Toast.LENGTH_SHORT);
		}
	}

	private void validarOpcionesMenuOffLine(){

		try {

			boolean menuVisible;

			if(pref.contains(Variables.MENU_SOCIOS_NEGOCIO)){
				JSONObject permiso = new JSONObject(pref.getString(Variables.MENU_SOCIOS_NEGOCIO, "N"));
				menuVisible = permiso.getString(Variables.MOVIL_ACCESA).equals("Y") ? true: false;
				navigationView.getMenu().findItem(R.id.nav_socios).setVisible(menuVisible);
			}else{
				navigationView.getMenu().findItem(R.id.nav_socios).setVisible(false);
			}

			if(pref.contains(Variables.MENU_INVENTARIO)){
				JSONObject permiso = new JSONObject(pref.getString(Variables.MENU_INVENTARIO, "N"));
				menuVisible = permiso.getString(Variables.MOVIL_ACCESA).equals("Y") ? true: false;
				navigationView.getMenu().findItem(R.id.nav_inventario).setVisible(menuVisible);
			}else{
				navigationView.getMenu().findItem(R.id.nav_inventario).setVisible(false);
			}

			if(pref.contains(Variables.MENU_PEDIDOS)){
				JSONObject permiso = new JSONObject(pref.getString(Variables.MENU_PEDIDOS, "N"));
				menuVisible = permiso.getString(Variables.MOVIL_ACCESA).equals("Y") ? true: false;
				navigationView.getMenu().findItem(R.id.nav_ordenes).setVisible(menuVisible);
			}else{
				navigationView.getMenu().findItem(R.id.nav_ordenes).setVisible(false);
			}

			if(pref.contains(Variables.MENU_FACTURAS)){
				JSONObject permiso = new JSONObject(pref.getString(Variables.MENU_FACTURAS, "N"));
				menuVisible = permiso.getString(Variables.MOVIL_ACCESA).equals("Y") ? true: false;
				navigationView.getMenu().findItem(R.id.nav_facturas).setVisible(menuVisible);
			}else{
				navigationView.getMenu().findItem(R.id.nav_facturas).setVisible(false);
			}

			if(pref.contains(Variables.MENU_COBRANZAS)){
				JSONObject permiso = new JSONObject(pref.getString(Variables.MENU_COBRANZAS, "N"));
				menuVisible = permiso.getString(Variables.MOVIL_ACCESA).equals("Y") ? true: false;
				navigationView.getMenu().findItem(R.id.nav_cobranzas).setVisible(menuVisible);
			}else{
				navigationView.getMenu().findItem(R.id.nav_cobranzas).setVisible(false);
			}

			if(pref.contains(Variables.MENU_REPORTES)){
				JSONObject permiso = new JSONObject(pref.getString(Variables.MENU_REPORTES, "N"));
				menuVisible = permiso.getString(Variables.MOVIL_ACCESA).equals("Y") ? true: false;
				navigationView.getMenu().findItem(R.id.nav_reportes).setVisible(menuVisible);
			}else{
				navigationView.getMenu().findItem(R.id.nav_reportes).setVisible(false);
			}

			if(pref.contains(Variables.MENU_ACTIVIDADES)){
				JSONObject permiso = new JSONObject(pref.getString(Variables.MENU_ACTIVIDADES, "N"));
				menuVisible = permiso.getString(Variables.MOVIL_ACCESA).equals("Y") ? true: false;
				navigationView.getMenu().findItem(R.id.nav_actividades).setVisible(menuVisible);
			}else{
				navigationView.getMenu().findItem(R.id.nav_actividades).setVisible(false);
			}

		}catch (Exception e){
			showToast("validarOpcionesMenuOffLine() > " + e.getMessage());
		}
	}

	private void setMenuVisible(boolean visible){
		navigationView.getMenu().getItem(1).setVisible(visible);
		navigationView.getMenu().getItem(2).setVisible(visible);
		navigationView.getMenu().getItem(3).setVisible(visible);
		navigationView.getMenu().getItem(4).setVisible(visible);
		navigationView.getMenu().getItem(5).setVisible(visible);
		navigationView.getMenu().getItem(6).setVisible(visible);
		navigationView.getMenu().getItem(7).setVisible(visible);
	}

	private void showToast(String message) {
		Toast.makeText(MainActivityDrawer.this, message, Toast.LENGTH_SHORT).show();
	}

	private void setToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		final ActionBar ab = getSupportActionBar();
		if (ab != null) {
			// Poner �cono del drawer toggle
			ab.setHomeAsUpIndicator(R.drawable.ic_menu);
			ab.setDisplayHomeAsUpEnabled(true);
		}

	}

	private void setupDrawerContent(NavigationView navigationView) {
		navigationView
				.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

					@Override
					public boolean onNavigationItemSelected(MenuItem menuItem) {
						// Marcar item presionado
						menuItem.setChecked(true);
						
						// Crear nuevo fragmento
						String title = menuItem.getTitle().toString();
						selectItem(title);
						return true;
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
			getMenuInflater().inflate(R.menu.menu_principal, menu);
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
				drawerLayout.openDrawer(GravityCompat.START);
			return true;
		case R.id.action_close_session:
				closeSession();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void closeSession(){
		
		AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
		alert.setTitle("Confirmaci�n");
		alert.setMessage("�Realmente desea salir de la aplicaci�n?");
		alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
			@SuppressLint("DefaultLocale")
			public void onClick(DialogInterface dialog, int whichButton) {

				SharedPreferences pref = PreferenceManager
						.getDefaultSharedPreferences(contexto);
				SharedPreferences.Editor editor = pref.edit();
				editor.remove(Variables.CODIGO_EMPLEADO);
				editor.remove(Variables.NOMBRE_EMPLEADO);
				editor.remove(Variables.USUARIO_EMPLEADO);
				editor.remove(Variables.PASSWORD_EMPLEADO);
				editor.commit();


//				Delete delete = new Delete(contexto);
//				delete.deleteAll();
				
				//DETENER LOS SERVICIOS
				Intent intent = new Intent(contexto, ServicioSocios.class);
				stopService(intent); 
				
				Intent intent2 = new Intent(contexto, ServicioOvPr.class);
				stopService(intent2);
				
				//LANZAR LA PANTALLA DE LOGIN
				Intent login = new Intent(contexto, LoginActivity.class);
				startActivity(login);
				
				finish();

			}
		});

		alert.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			
			
			}
		});
		alert.show();
		
	}

	private void selectItem(String title) {
		
		// Enviar t�tulo como argumento del fragmento
		Bundle args = new Bundle();
		args.putString(PlaceholderFragment.ARG_SECTION_TITLE, title);
		
		if(title.equalsIgnoreCase(getResources().getString(R.string.menu_inicio))){
			
			Fragment fragment = PlaceholderFragment.newInstance(title);
			fragment.setArguments(args);
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.main_content, fragment)
					.commit();
			setTitle(title); // Setear t�tulo actual
			
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_socio_negocio))){
			
			Fragment fragment = new ListaSocioNegocio();
			FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.commit();
            setTitle(title);
            
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_inventario))){
			
			Fragment fragment = new ListaArticulosMain();
			FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.commit();
            setTitle(title); 
			
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_actividades))){
			
			calendarEventM();
			
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_ventas))){
			
			Fragment fragment = ListaVentasMain.newInstance();
			FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.commit();
            setTitle(title); // Setear t�tulo actual
			
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_facturas))){
			
			Fragment fragment = new ListaFacturasMain();
			FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.commit();
            setTitle(title); // Setear t�tulo actual
			
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_cobranzas))){
			
			Fragment fragment = new ListaCobranzasMain();
			FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.commit();
            setTitle(title); // Setear t�tulo actual
			
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_conexion))){
			
			Intent conn = new Intent(contexto,
					SettingsMain.class);
			startActivity(conn);
			
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_sincronizar_ini))){
			sincronizarInicio();
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_sincronizar_doc))){
			sincronizarDocumentos();
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_sincronizar_mast))){
			sincronizarMaestros();
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_cerrar_sesion))){
			closeSession();
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_reportes))){
			
			Fragment fragment = new ReporteFragment();
			FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.commit();
            setTitle(title); // Setear t�tulo actual
			
		}else{
			Fragment fragment = PlaceholderFragment.newInstance(title);
			fragment.setArguments(args);
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.main_content, fragment)
					.commit();
		}

		drawerLayout.closeDrawers(); // Cerrar drawer
	}
	
	
	@SuppressLint("NewApi") 
	private void calendarEventM() {
/*		Calendar calendarEvent = Calendar.getInstance();
		Intent i = new Intent(Intent.ACTION_EDIT);
		i.setType("vnd.android.cursor.item/event");
		i.putExtra("beginTime", calendarEvent.getTimeInMillis());
		i.putExtra("allDay", false);
		i.putExtra("rule", "FREQ=YEARLY");
		i.putExtra("endTime", calendarEvent.getTimeInMillis() + 60 * 60 * 1000);
		i.putExtra("title", "Editar el t�tulo de su actividad aqu�");
		startActivity(i);			*/
		
		long startMillis = System.currentTimeMillis();
		Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
		builder.appendPath("time");
		ContentUris.appendId(builder, startMillis);
		Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
		startActivity(intent);
		
	}
	
	private void sincronizarInicio(){
		// VERIFICAR EL ESTADO DE LA CONEXION DEL MOVIL
		boolean wifi = Connectivity.isConnectedWifi(contexto);
		boolean movil = Connectivity.isConnectedMobile(contexto);
		boolean isConnectionFast = Connectivity
				.isConnectedFast(contexto);

		if (wifi || movil) {

			if (isConnectionFast) {

				pd = new ProgressDialog(contexto);
				pd.setTitle("Sincronizando");
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.setCancelable(false);
				pd.setIndeterminate(false);
				pd.setMessage("Por favor, espere...");
				pd.setProgress(0);
				pd.setMax(17);
				pd.show();

				SyncRestInicio syncInitial = new SyncRestInicio(contexto, pd);
				boolean res = syncInitial.syncFromServer();

				if(res)
					Toast.makeText(contexto, "Sincronizaci�n finalizada.", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(contexto, "Ocurri� un error intentando conectar con el servidor", Toast.LENGTH_SHORT).show();
				//SincManualTaskInicio job = new SincManualTaskInicio(pd, contexto, "");
				//job.execute();
				
			} else {
				Toast.makeText(contexto, "La conexi�n es inestable", Toast.LENGTH_LONG).show();
			}
		}else {
			Toast.makeText(contexto, "No est� conectado a ninguna red de datos...", Toast.LENGTH_LONG).show();
		}
	}
	
	private void sincronizarDocumentos(){
		// VERIFICAR EL ESTADO DE LA CONEXION DEL MOVIL
		boolean wifi = Connectivity.isConnectedWifi(contexto);
		boolean movil = Connectivity.isConnectedMobile(contexto);
		boolean isConnectionFast = Connectivity
				.isConnectedFast(contexto);

		if (wifi || movil) {

			if (isConnectionFast) {

				pd = new ProgressDialog(contexto);
				pd.setTitle("Sincronizando");
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.setCancelable(false);
				pd.setIndeterminate(false);
				pd.setMessage("Por favor, espere...");
				pd.setProgress(0);
				pd.setMax(5);
				pd.show();

				//SincManualTaskDocumentos job = new SincManualTaskDocumentos(pd, contexto, "");

				SyncRestMaestros syncRestMaestros = new SyncRestMaestros(contexto, pd);
				boolean res = syncRestMaestros.syncFromServer();

				if(res)
					Toast.makeText(contexto, "Sincronizaci�n finalizada.", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(contexto, "Ocurri� un error intentando conectar con el servidor", Toast.LENGTH_SHORT).show();

			/*	if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
				    job.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} else {
					job.execute();
				}	*/
				
			} else {
				Toast.makeText(contexto, "La conexi�n es inestable", Toast.LENGTH_LONG).show();
			}
		}else {
			
			Toast.makeText(contexto, "No est� conectado a ninguna red de datos...", Toast.LENGTH_LONG).show();

		}
	}

	private void sincronizarMaestros(){
		// VERIFICAR EL ESTADO DE LA CONEXION DEL MOVIL
		boolean wifi = Connectivity.isConnectedWifi(contexto);
		boolean movil = Connectivity.isConnectedMobile(contexto);
		boolean isConnectionFast = Connectivity
				.isConnectedFast(contexto);

		if (wifi || movil) {

			if (isConnectionFast) {
				
//				Intent intent2 = new Intent(contexto, ServicioSociosWithTask.class);
//				intent2.setAction(Constants.ACTION_RUN_ISERVICE);
//				startService(intent2);

				pd = new ProgressDialog(contexto);
				pd.setTitle("Sincronizando");
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.setCancelable(false);
				pd.setIndeterminate(false);
				pd.setMessage("Por favor, espere");
				pd.setProgress(0);
				pd.setMax(6);
				pd.show();

//				SincManualTaskMaestros job = new SincManualTaskMaestros(pd, contexto, "");
//
//				if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
//				    job.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//				} else {
//					job.execute();
//				}

				SyncRestMaestros syncRestMaestros = new SyncRestMaestros(contexto, pd);
				boolean res = syncRestMaestros.syncFromServer();

				if(res)
					Toast.makeText(contexto, "Sincronizaci�n finalizada.", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(contexto, "Ocurri� un error intentando conectar con el servidor", Toast.LENGTH_SHORT).show();


			} else {
				Toast.makeText(contexto, "La conexi�n es inestable", Toast.LENGTH_LONG).show();
			}
		}else {
			
			Toast.makeText(contexto, "No est� conectado a ninguna red de datos...", Toast.LENGTH_LONG).show();

		}
	}

}
