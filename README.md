
# UTIL Almera
Luis
  

[![N|Solid](https://www.almeraim.com/wp-content/uploads/2017/07/logoalmera.png)](https://nodesource.com/products/nsolid)
[![](https://jitpack.io/v/gfigueroaalmera/utilalmera.svg)](https://jitpack.io/#gfigueroaalmera/utilalmera)
  
Libreria con los metodos util que se utilizan en todas las aplicaciones.

## Requerimientos

  

- Configurar gradle.build del Proyecto agregandondo esto:



- Agregamos esta configuracion en gradle.build de la aplicación y la dependecia de esta libreria

```


dependencies {
implementation 'com.github.gfigueroaalmera:utilalmera:1.0'
}

```

 - Descargar imagen a el almacenamiento local privado.
	Le ingresa la uri donde esta el archivo a descargar puede ser jpg, png o svg, el context y el filename donde va quedar el archivo puede colocar "/" para crear subcarpetas.
	Este metodo se debe hacer dentro de otro hilo fuera de la GUI entonces como tenemos Rx java podemos usar la sintaxis de RX.
     ```
   Observable.fromCallable(()->{   
   ArchivosUtil.downloadImageToLocalPrivate("https://imagen.png",context,"sgidemo_gfigueroa_drawer");
   return true;
   }).subscribeOn(Schedulers.io())  
        .observeOn(AndroidSchedulers.mainThread()).subscribe((result) -> {
        log.d("finish","Se descargo la imagen")});
     ```
 - Cargar imagen desde almacenamiento local privado, le ingresa el textview y el file name del archivo a cargar.
	```
	ArchivosUtil.loadImageFromDisk(imgView, sgidemo_gfigueroa_drawer);
	```
**Descargar Imagenes desde html**
 - Hacemos una list de **ImageDownload** con el valor html y el filename que se desee colocarle al archivo
 ```java
 for (int i = 0; i < responseTramites.getTramites().size(); i++) {  
    for (int j = 0; j < responseTramites.getTramites().get(i).getCampos().size(); j++) {  
        if (responseTramites.getTramites().get(i).getCampos().get(j).getTipo().equals("label")) {  
            camposLabel.add(new ImageDownload(responseTramites.getTramites().get(i).getCampos().get(j).getPlantilla(), LibLoginSharedPreferencesUtil.getSharedLogin(getApplicationContext(), LibLoginConstantesUtil.SH_CONEXION) + responseTramites.getTramites().get(i).getCampos().get(j).getId()));  
        }  
    }  
}
 ```
 
 - Ahora llamamos el metodo para descargar las imagenes **DonwloadPicturesFromHtml**
 ```java
 LibArchivosUtil.DonwloadPicturesFromHtml(getApplicationContext(), camposLabel, new LibFinishDowload() {  
      @Override  
  public void onFinish(Boolean status) {  
          dialogLisener.hideDialog();  
          Toast.makeText(HomeActivity.this, R.string.sincronizacion_existosa,  
                  Toast.LENGTH_LONG).show();  
      }  
  });
 ```
		 
