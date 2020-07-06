### *UTN-FRBA*
### *Desarrollo de Aplicaciones para Dispositivos Móviles*

# RestoPass


## Profesor:
Emanuel Andrada

## Integrantes
| Nombre y apellido | Legajo |
| ---------------- | -------- |
| Cabanas, Juan Ignacio | 152.160-3 |
| Casarini, Yamila | 152.643-1 |
| Voskoboinik, Tobías | 152.583-9 |

----

## Detalles del proyecto

### Descripción
    RestoPass permite, a partir de la elección del pago de una membresía mensual, asistir a diferentes
    restaurantes a un precio más bajo del que irían sin este plan.
    A partir de nuestra aplicación podrán gestionar sus reservas, invitar a otros usuarios RestoPass a un almuerzo/cena (quienes serán notificados), 
    agregar restuarantes a sus favoritos y muchas otras cosas más.

### Alcance
#### Incluye
- Login del usuario con credenciales Google o RestoPass
- Creación de cuenta con credenciales Google o RestoPass
- Selección de membresías
- Restaurantes Favoritos
- Restaurantes cerca de la ubicación del usuario
- Mejores restaurantes de la semana 
- Mapa con nuestros restaurantes adheridos.
- Creación de reservas, invitando a usuarios RestoPass
- Notificaciones Firebase ante la invitación a una reserva, aceptación de un usuario a la reserva o cancelación de la reserva.
- Puntuación de restaurante y plato luego de asistir al restaurant (el restaurant lee el correspondiente código QR de la reserva, disparando un evento de puntuación)
- [Servidor Backend](https://github.com/yamilacasarini/restopassServer) con la persistencia de todo el modelo de datos.
   Dicho servidor, también se encarga de disparar los eventos correspondientes de Firebase y 
   de renderizar el frontend al confirmar o cancelar una reserva. 
   También renderiza la página correspondiente al momento en el que 
   el mozo lee el código QR, mostrándole todos los platos que los participantes de la reserva pueden elegir y disparando los eventos previamente mencionados.

##### No incluye
- Perfil Restaurante
- Métodos de pago
- Edición info personal
- Recupero contraseña

----
### - [Screenshots](screenshots/screenshots.md)

### - [RestoPass demo](screenshots/)
