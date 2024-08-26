import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Empleado } from './empleado';

@Injectable({
  providedIn: 'root'
})
export class EmpleadoService {

//Esta url obtiene el listado de todos los empleados del backend

  private baseURL = "http://localhost:8080/api/v1/empleados";

  constructor(private httpClient : HttpClient) { }

  //metodo sirve para obtener los empleados
  obtenerListaEmpleados():Observable<Empleado[]>{
    return this.httpClient.get<Empleado[]>(`${this.baseURL}`);

  }
//Este metodo sirve para registrar un empleado
  registrarEmpleado(empleado:Empleado) : Observable<Object>{
    return this.httpClient.post(`${this.baseURL}`,empleado);
  }
  //metodo sirve para actualizar empleado
  actualizarEmpleado(id:number,empleado:Empleado) : Observable<Object>{
    return this.httpClient.put(`${this.baseURL}/${id}`,empleado);
  }
   //metodo para eliminar un empleado
    eliminarEmpleado(id:number): Observable<Object>{
      return this.httpClient.delete(`${this.baseURL}/${id}`);

    }
     //este metodo sirve para obtener o buscar un empleado
  obtenerEmpleadoPorId(id:number):Observable<Empleado>{
    return this.httpClient.get<Empleado>(`${this.baseURL}/${id}`);
  }

    // Método para descargar la lista de empleados en formato Word
    descargarListaDeEmpleadosWord(): Observable<Blob> {
      return this.httpClient.get(`${this.baseURL}/export-word`, {
        responseType: 'blob'
      });
    }
}
      
