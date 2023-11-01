package me.relrypesan.transporteescolarlistapresenca.core.domain.ports;

public interface SalvarPort<T> {
    T cadastrar(T object);
    T atualizar(T object);
}
