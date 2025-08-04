import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EnvironmentService } from './environment.service';
import { ShelvingUnitDto, RequestShelvingUnitDto, RequestPatchShelvingUnitDto } from '../models/dto/shelving-unit.dto';

@Injectable({
  providedIn: 'root'
})
export class ShelvingService {
  private baseUrl: string;

  constructor(
    private http: HttpClient,
    private environmentService: EnvironmentService
  ) {
    this.baseUrl = `${this.environmentService.getApiBaseUrl()}/shelving-unit`;
  }

  findAll(): Observable<ShelvingUnitDto[]> {
    return this.http.get<ShelvingUnitDto[]>(this.baseUrl);
  }

  findById(id: number): Observable<ShelvingUnitDto> {
    return this.http.get<ShelvingUnitDto>(`${this.baseUrl}/${id}`);
  }

  create(data: RequestShelvingUnitDto): Observable<ShelvingUnitDto> {
    return this.http.post<ShelvingUnitDto>(this.baseUrl, data);
  }

  update(id: number, data: RequestShelvingUnitDto): Observable<ShelvingUnitDto> {
    return this.http.put<ShelvingUnitDto>(`${this.baseUrl}/${id}`, data);
  }

  patch(id: number, data: RequestPatchShelvingUnitDto): Observable<ShelvingUnitDto> {
    return this.http.patch<ShelvingUnitDto>(`${this.baseUrl}/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}