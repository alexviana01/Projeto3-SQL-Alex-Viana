PGDMP      /        	        }            modulo30SQLEbac    17.5    17.5 +    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            �           1262    41251    modulo30SQLEbac    DATABASE     �   CREATE DATABASE "modulo30SQLEbac" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Portuguese_Brazil.1252';
 !   DROP DATABASE "modulo30SQLEbac";
                     postgres    false            �            1259    41252 
   tb_cliente    TABLE     W  CREATE TABLE public.tb_cliente (
    id bigint NOT NULL,
    nome character varying(50) NOT NULL,
    cpf bigint NOT NULL,
    tel bigint NOT NULL,
    endereco character varying(50) NOT NULL,
    numero bigint NOT NULL,
    cidade character varying(50) NOT NULL,
    estado character varying(50) NOT NULL,
    email character varying(100)
);
    DROP TABLE public.tb_cliente;
       public         heap r       postgres    false            �            1259    41287 
   sq_cliente    SEQUENCE     s   CREATE SEQUENCE public.sq_cliente
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 !   DROP SEQUENCE public.sq_cliente;
       public               postgres    false    217            �           0    0 
   sq_cliente    SEQUENCE OWNED BY     @   ALTER SEQUENCE public.sq_cliente OWNED BY public.tb_cliente.id;
          public               postgres    false    221            �            1259    41299 
   tb_estoque    TABLE     �   CREATE TABLE public.tb_estoque (
    id bigint NOT NULL,
    id_produto_fk bigint NOT NULL,
    quantidade integer NOT NULL,
    localizacao character varying(100),
    data_ultima_atualizacao timestamp with time zone NOT NULL
);
    DROP TABLE public.tb_estoque;
       public         heap r       postgres    false            �            1259    41311 
   sq_estoque    SEQUENCE     s   CREATE SEQUENCE public.sq_estoque
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 !   DROP SEQUENCE public.sq_estoque;
       public               postgres    false    225            �           0    0 
   sq_estoque    SEQUENCE OWNED BY     @   ALTER SEQUENCE public.sq_estoque OWNED BY public.tb_estoque.id;
          public               postgres    false    226            �            1259    41257 
   tb_produto    TABLE       CREATE TABLE public.tb_produto (
    id bigint NOT NULL,
    codigo character varying(10) NOT NULL,
    nome character varying(50) NOT NULL,
    descricao character varying(100) NOT NULL,
    valor numeric(10,2) NOT NULL,
    categoria character varying(50)
);
    DROP TABLE public.tb_produto;
       public         heap r       postgres    false            �            1259    41288 
   sq_produto    SEQUENCE     s   CREATE SEQUENCE public.sq_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 !   DROP SEQUENCE public.sq_produto;
       public               postgres    false    218            �           0    0 
   sq_produto    SEQUENCE OWNED BY     @   ALTER SEQUENCE public.sq_produto OWNED BY public.tb_produto.id;
          public               postgres    false    222            �            1259    41272    tb_produto_quantidade    TABLE     �   CREATE TABLE public.tb_produto_quantidade (
    id bigint NOT NULL,
    id_produto_fk bigint NOT NULL,
    id_venda_fk bigint NOT NULL,
    quantidade integer NOT NULL,
    valor_total numeric(10,2) NOT NULL
);
 )   DROP TABLE public.tb_produto_quantidade;
       public         heap r       postgres    false            �            1259    41290    sq_produto_quantidade    SEQUENCE     ~   CREATE SEQUENCE public.sq_produto_quantidade
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.sq_produto_quantidade;
       public               postgres    false    220            �           0    0    sq_produto_quantidade    SEQUENCE OWNED BY     V   ALTER SEQUENCE public.sq_produto_quantidade OWNED BY public.tb_produto_quantidade.id;
          public               postgres    false    224            �            1259    41262    tb_venda    TABLE       CREATE TABLE public.tb_venda (
    id bigint NOT NULL,
    codigo character varying(10) NOT NULL,
    id_cliente_fk bigint NOT NULL,
    valor_total numeric(10,2) NOT NULL,
    data_venda timestamp with time zone NOT NULL,
    status_venda character varying(50) NOT NULL
);
    DROP TABLE public.tb_venda;
       public         heap r       postgres    false            �            1259    41289    sq_venda    SEQUENCE     q   CREATE SEQUENCE public.sq_venda
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    DROP SEQUENCE public.sq_venda;
       public               postgres    false    219            �           0    0    sq_venda    SEQUENCE OWNED BY     <   ALTER SEQUENCE public.sq_venda OWNED BY public.tb_venda.id;
          public               postgres    false    223            �          0    41252 
   tb_cliente 
   TABLE DATA           a   COPY public.tb_cliente (id, nome, cpf, tel, endereco, numero, cidade, estado, email) FROM stdin;
    public               postgres    false    217   52       �          0    41299 
   tb_estoque 
   TABLE DATA           i   COPY public.tb_estoque (id, id_produto_fk, quantidade, localizacao, data_ultima_atualizacao) FROM stdin;
    public               postgres    false    225   R2       �          0    41257 
   tb_produto 
   TABLE DATA           S   COPY public.tb_produto (id, codigo, nome, descricao, valor, categoria) FROM stdin;
    public               postgres    false    218   o2       �          0    41272    tb_produto_quantidade 
   TABLE DATA           h   COPY public.tb_produto_quantidade (id, id_produto_fk, id_venda_fk, quantidade, valor_total) FROM stdin;
    public               postgres    false    220   �2       �          0    41262    tb_venda 
   TABLE DATA           d   COPY public.tb_venda (id, codigo, id_cliente_fk, valor_total, data_venda, status_venda) FROM stdin;
    public               postgres    false    219   �2       �           0    0 
   sq_cliente    SEQUENCE SET     9   SELECT pg_catalog.setval('public.sq_cliente', 1, false);
          public               postgres    false    221            �           0    0 
   sq_estoque    SEQUENCE SET     9   SELECT pg_catalog.setval('public.sq_estoque', 1, false);
          public               postgres    false    226            �           0    0 
   sq_produto    SEQUENCE SET     8   SELECT pg_catalog.setval('public.sq_produto', 1, true);
          public               postgres    false    222            �           0    0    sq_produto_quantidade    SEQUENCE SET     D   SELECT pg_catalog.setval('public.sq_produto_quantidade', 1, false);
          public               postgres    false    224            �           0    0    sq_venda    SEQUENCE SET     7   SELECT pg_catalog.setval('public.sq_venda', 1, false);
          public               postgres    false    223            6           2606    41256    tb_cliente pk_id_cliente 
   CONSTRAINT     V   ALTER TABLE ONLY public.tb_cliente
    ADD CONSTRAINT pk_id_cliente PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.tb_cliente DROP CONSTRAINT pk_id_cliente;
       public                 postgres    false    217            F           2606    41303    tb_estoque pk_id_estoque 
   CONSTRAINT     V   ALTER TABLE ONLY public.tb_estoque
    ADD CONSTRAINT pk_id_estoque PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.tb_estoque DROP CONSTRAINT pk_id_estoque;
       public                 postgres    false    225            D           2606    41276 &   tb_produto_quantidade pk_id_prod_venda 
   CONSTRAINT     d   ALTER TABLE ONLY public.tb_produto_quantidade
    ADD CONSTRAINT pk_id_prod_venda PRIMARY KEY (id);
 P   ALTER TABLE ONLY public.tb_produto_quantidade DROP CONSTRAINT pk_id_prod_venda;
       public                 postgres    false    220            <           2606    41261    tb_produto pk_id_produto 
   CONSTRAINT     V   ALTER TABLE ONLY public.tb_produto
    ADD CONSTRAINT pk_id_produto PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.tb_produto DROP CONSTRAINT pk_id_produto;
       public                 postgres    false    218            @           2606    41266    tb_venda pk_id_venda 
   CONSTRAINT     R   ALTER TABLE ONLY public.tb_venda
    ADD CONSTRAINT pk_id_venda PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.tb_venda DROP CONSTRAINT pk_id_venda;
       public                 postgres    false    219            >           2606    41294    tb_produto uk_codigo_produto 
   CONSTRAINT     Y   ALTER TABLE ONLY public.tb_produto
    ADD CONSTRAINT uk_codigo_produto UNIQUE (codigo);
 F   ALTER TABLE ONLY public.tb_produto DROP CONSTRAINT uk_codigo_produto;
       public                 postgres    false    218            B           2606    41296    tb_venda uk_codigo_venda 
   CONSTRAINT     U   ALTER TABLE ONLY public.tb_venda
    ADD CONSTRAINT uk_codigo_venda UNIQUE (codigo);
 B   ALTER TABLE ONLY public.tb_venda DROP CONSTRAINT uk_codigo_venda;
       public                 postgres    false    219            8           2606    41292    tb_cliente uk_cpf_cliente 
   CONSTRAINT     S   ALTER TABLE ONLY public.tb_cliente
    ADD CONSTRAINT uk_cpf_cliente UNIQUE (cpf);
 C   ALTER TABLE ONLY public.tb_cliente DROP CONSTRAINT uk_cpf_cliente;
       public                 postgres    false    217            :           2606    41298    tb_cliente uk_email_cliente 
   CONSTRAINT     W   ALTER TABLE ONLY public.tb_cliente
    ADD CONSTRAINT uk_email_cliente UNIQUE (email);
 E   ALTER TABLE ONLY public.tb_cliente DROP CONSTRAINT uk_email_cliente;
       public                 postgres    false    217            H           2606    41305 !   tb_estoque uk_produto_localizacao 
   CONSTRAINT     r   ALTER TABLE ONLY public.tb_estoque
    ADD CONSTRAINT uk_produto_localizacao UNIQUE (id_produto_fk, localizacao);
 K   ALTER TABLE ONLY public.tb_estoque DROP CONSTRAINT uk_produto_localizacao;
       public                 postgres    false    225    225            I           2606    41267    tb_venda fk_id_cliente_venda    FK CONSTRAINT     �   ALTER TABLE ONLY public.tb_venda
    ADD CONSTRAINT fk_id_cliente_venda FOREIGN KEY (id_cliente_fk) REFERENCES public.tb_cliente(id);
 F   ALTER TABLE ONLY public.tb_venda DROP CONSTRAINT fk_id_cliente_venda;
       public               postgres    false    219    4662    217            J           2606    41277 &   tb_produto_quantidade fk_id_prod_venda    FK CONSTRAINT     �   ALTER TABLE ONLY public.tb_produto_quantidade
    ADD CONSTRAINT fk_id_prod_venda FOREIGN KEY (id_produto_fk) REFERENCES public.tb_produto(id);
 P   ALTER TABLE ONLY public.tb_produto_quantidade DROP CONSTRAINT fk_id_prod_venda;
       public               postgres    false    4668    220    218            K           2606    41282 ,   tb_produto_quantidade fk_id_prod_venda_venda    FK CONSTRAINT     �   ALTER TABLE ONLY public.tb_produto_quantidade
    ADD CONSTRAINT fk_id_prod_venda_venda FOREIGN KEY (id_venda_fk) REFERENCES public.tb_venda(id);
 V   ALTER TABLE ONLY public.tb_produto_quantidade DROP CONSTRAINT fk_id_prod_venda_venda;
       public               postgres    false    4672    219    220            L           2606    41306     tb_estoque fk_id_produto_estoque    FK CONSTRAINT     �   ALTER TABLE ONLY public.tb_estoque
    ADD CONSTRAINT fk_id_produto_estoque FOREIGN KEY (id_produto_fk) REFERENCES public.tb_produto(id);
 J   ALTER TABLE ONLY public.tb_estoque DROP CONSTRAINT fk_id_produto_estoque;
       public               postgres    false    4668    218    225            �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �     