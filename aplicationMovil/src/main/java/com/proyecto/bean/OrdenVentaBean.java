package com.proyecto.bean;

import java.util.ArrayList;

public class OrdenVentaBean {

	private String nroDocOrdV, estadoDoc, codSN, nomSN, contacto, moneda, fecContable, fecVen, fecDoc,
			numRef, comentario, dirEntrega, destFactura, empVentas, tipoDoc, dirFiscal, condPago, indicador, nroDoc,
			clave, numero, referencia, listaPrecio, subTotal, descuento, impuesto, total, saldo,
			creadoMovil, claveMovil, estadoRegistroMovil, TransaccionMovil;
	
	public String getCreadoMovil() {
		return creadoMovil;
	}

	public void setCreadoMovil(String creadoMovil) {
		this.creadoMovil = creadoMovil;
	}

	public String getClaveMovil() {
		return claveMovil;
	}

	public void setClaveMovil(String claveMovil) {
		this.claveMovil = claveMovil;
	}

	public String getEstadoRegistroMovil() {
		return estadoRegistroMovil;
	}

	public void setEstadoRegistroMovil(String estadoRegistroMovil) {
		this.estadoRegistroMovil = estadoRegistroMovil;
	}

	private ArrayList<OrdenVentaDetalleBean> detalles;
	

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getDescuento() {
		return descuento;
	}

	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}

	public String getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(String impuesto) {
		this.impuesto = impuesto;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getSaldo() {
		return saldo;
	}

	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getCondPago() {
		return condPago;
	}

	public void setCondPago(String condPago) {
		this.condPago = condPago;
	}

	public String getIndicador() {
		return indicador;
	}

	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}

	public String getNroDoc() {
		return nroDoc;
	}

	public void setNroDoc(String nroDoc) {
		this.nroDoc = nroDoc;
	}

	private double totAntesDesc, porcDesc, totDesc, totImp, totGeneral;

	private int utilIcon,utilIcon2;

	public String getEstadoDoc() {
		return estadoDoc;
	}

	public void setEstadoDoc(String estadoDoc) {
		this.estadoDoc = estadoDoc;
	}

	public String getCodSN() {
		return codSN;
	}

	public void setCodSN(String codSN) {
		this.codSN = codSN;
	}

	public String getNomSN() {
		return nomSN;
	}

	public void setNomSN(String nomSN) {
		this.nomSN = nomSN;
	}

	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getFecContable() {
		return fecContable;
	}

	public void setFecContable(String fecContable) {
		this.fecContable = fecContable;
	}

	public String getFecVen() {
		return fecVen;
	}

	public void setFecVen(String fecVen) {
		this.fecVen = fecVen;
	}

	public String getFecDoc() {
		return fecDoc;
	}

	public void setFecDoc(String fecDoc) {
		this.fecDoc = fecDoc;
	}

	public String getNumRef() {
		return numRef;
	}

	public void setNumRef(String numRef) {
		this.numRef = numRef;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getDirEntrega() {
		return dirEntrega;
	}

	public void setDirEntrega(String dirEntrega) {
		this.dirEntrega = dirEntrega;
	}

	public String getDestFactura() {
		return destFactura;
	}

	public void setDestFactura(String destFactura) {
		this.destFactura = destFactura;
	}

	public double getTotAntesDesc() {
		return totAntesDesc;
	}

	public void setTotAntesDesc(double totAntesDesc) {
		this.totAntesDesc = totAntesDesc;
	}

	public double getPorcDesc() {
		return porcDesc;
	}

	public void setPorcDesc(double porcDesc) {
		this.porcDesc = porcDesc;
	}

	public double getTotDesc() {
		return totDesc;
	}

	public void setTotDesc(double totDesc) {
		this.totDesc = totDesc;
	}

	public double getTotImp() {
		return totImp;
	}

	public void setTotImp(double totImp) {
		this.totImp = totImp;
	}

	public double getTotGeneral() {
		return totGeneral;
	}

	public void setTotGeneral(double totGeneral) {
		this.totGeneral = totGeneral;
	}

	public String getNroDocOrdV() {
		return nroDocOrdV;
	}

	public void setNroDocOrdV(String nroDocOrdV) {
		this.nroDocOrdV = nroDocOrdV;
	}

	public int getUtilIcon() {
		return utilIcon;
	}

	public void setUtilIcon(int utilIcon) {
		this.utilIcon = utilIcon;
	}

	public String getEmpVentas() {
		return empVentas;
	}

	public void setEmpVentas(String empVentas) {
		this.empVentas = empVentas;
	}

	public String getTipoDoc() {
		return tipoDoc;
	}

	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

	public String getDirFiscal() {
		return dirFiscal;
	}

	public void setDirFiscal(String dirFiscal) {
		this.dirFiscal = dirFiscal;
	}

	public ArrayList<OrdenVentaDetalleBean> getDetalles() {
		return detalles;
	}

	public void setDetalles(ArrayList<OrdenVentaDetalleBean> detalles) {
		this.detalles = detalles;
	}

	public String getListaPrecio() {
		return listaPrecio;
	}

	public void setListaPrecio(String listaPrecio) {
		this.listaPrecio = listaPrecio;
	}

	public int getUtilIcon2() {
		return utilIcon2;
	}

	public void setUtilIcon2(int utilIcon2) {
		this.utilIcon2 = utilIcon2;
	}

	public String getTransaccionMovil() {
		return TransaccionMovil;
	}

	public void setTransaccionMovil(String transaccionMovil) {
		TransaccionMovil = transaccionMovil;
	}

	
	
	
	
	
}
