PGDMP  5    5                }           postgres    17.0    17.0 Y    Z           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            [           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            \           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            ]           1262    5    postgres    DATABASE     {   CREATE DATABASE postgres WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Italian_Italy.1252';
    DROP DATABASE postgres;
                     postgres    false            ^           0    0    DATABASE postgres    COMMENT     N   COMMENT ON DATABASE postgres IS 'default administrative connection database';
                        postgres    false    4957                        2615    16504    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
                     pg_database_owner    false            _           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                        pg_database_owner    false    5            �            1259    16708    carrello    TABLE     �   CREATE TABLE public.carrello (
    id integer NOT NULL,
    idutente integer,
    idprodotto integer,
    quantita integer,
    isordinato boolean,
    taglia character varying(5) DEFAULT 'M'::character varying,
    rimosso boolean DEFAULT false
);
    DROP TABLE public.carrello;
       public         heap r       postgres    false    5            �            1259    16707    carrello_id_seq    SEQUENCE     �   CREATE SEQUENCE public.carrello_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.carrello_id_seq;
       public               postgres    false    236    5            `           0    0    carrello_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.carrello_id_seq OWNED BY public.carrello.id;
          public               postgres    false    235            �            1259    16663 	   categoria    TABLE     q   CREATE TABLE public.categoria (
    id integer NOT NULL,
    name character varying(10),
    description text
);
    DROP TABLE public.categoria;
       public         heap r       postgres    false    5            �            1259    16662    categoria_id_seq    SEQUENCE     �   CREATE SEQUENCE public.categoria_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.categoria_id_seq;
       public               postgres    false    5    230            a           0    0    categoria_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.categoria_id_seq OWNED BY public.categoria.id;
          public               postgres    false    229            �            1259    16691    dettagliordini    TABLE     �   CREATE TABLE public.dettagliordini (
    id integer NOT NULL,
    idordine integer,
    idprodotto integer,
    quantita integer
);
 "   DROP TABLE public.dettagliordini;
       public         heap r       postgres    false    5            �            1259    16690    dettagliordini_id_seq    SEQUENCE     �   CREATE SEQUENCE public.dettagliordini_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.dettagliordini_id_seq;
       public               postgres    false    234    5            b           0    0    dettagliordini_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.dettagliordini_id_seq OWNED BY public.dettagliordini.id;
          public               postgres    false    233            �            1259    16509    disponibilita    TABLE     �   CREATE TABLE public.disponibilita (
    id integer NOT NULL,
    quantita integer,
    taglia character varying(10),
    idprodotto integer
);
 !   DROP TABLE public.disponibilita;
       public         heap r       postgres    false    5            �            1259    16512    disponibilita_id_seq    SEQUENCE     �   CREATE SEQUENCE public.disponibilita_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.disponibilita_id_seq;
       public               postgres    false    5    217            c           0    0    disponibilita_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.disponibilita_id_seq OWNED BY public.disponibilita.id;
          public               postgres    false    218            �            1259    16513 	   indirizzo    TABLE     @  CREATE TABLE public.indirizzo (
    id integer NOT NULL,
    nomevia character varying(100),
    civico character varying(10),
    citta character varying(50),
    cap character varying(10),
    provincia character varying(50),
    regione character varying(50),
    idutente integer,
    attivo boolean DEFAULT true
);
    DROP TABLE public.indirizzo;
       public         heap r       postgres    false    5            �            1259    16516    indirizzo_id_seq    SEQUENCE     �   CREATE SEQUENCE public.indirizzo_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.indirizzo_id_seq;
       public               postgres    false    5    219            d           0    0    indirizzo_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.indirizzo_id_seq OWNED BY public.indirizzo.id;
          public               postgres    false    220            �            1259    16517    metododipagamento    TABLE     X  CREATE TABLE public.metododipagamento (
    id integer NOT NULL,
    tipopagamento character varying(50),
    titolare character varying(100),
    tipocarta character varying(50),
    numcarta character varying(16),
    datascadenza character varying(50),
    cvv character varying(50),
    idutente integer,
    attivo boolean DEFAULT true
);
 %   DROP TABLE public.metododipagamento;
       public         heap r       postgres    false    5            �            1259    16520    metododipagamento_id_seq    SEQUENCE     �   CREATE SEQUENCE public.metododipagamento_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.metododipagamento_id_seq;
       public               postgres    false    5    221            e           0    0    metododipagamento_id_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.metododipagamento_id_seq OWNED BY public.metododipagamento.id;
          public               postgres    false    222            �            1259    16628    ordine    TABLE        CREATE TABLE public.ordine (
    id integer NOT NULL,
    idutente integer NOT NULL,
    dataoridne timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    stato character varying(255),
    totaledapagare numeric(10,2),
    idmetodopagamento integer
);
    DROP TABLE public.ordine;
       public         heap r       postgres    false    5            �            1259    16627    ordine_id_seq    SEQUENCE     �   CREATE SEQUENCE public.ordine_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.ordine_id_seq;
       public               postgres    false    5    228            f           0    0    ordine_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.ordine_id_seq OWNED BY public.ordine.id;
          public               postgres    false    227            �            1259    16524    prodotto    TABLE     1  CREATE TABLE public.prodotto (
    id integer NOT NULL,
    nome character varying(100),
    marca character varying(50),
    colore character varying(20),
    prezzo numeric(10,2),
    descrizione character varying(200),
    scontato boolean,
    image character varying(255),
    idcategoria integer
);
    DROP TABLE public.prodotto;
       public         heap r       postgres    false    5            �            1259    16527    prodotto_id_seq    SEQUENCE     �   CREATE SEQUENCE public.prodotto_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.prodotto_id_seq;
       public               postgres    false    5    223            g           0    0    prodotto_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.prodotto_id_seq OWNED BY public.prodotto.id;
          public               postgres    false    224            �            1259    16672 
   recensione    TABLE     �   CREATE TABLE public.recensione (
    id integer NOT NULL,
    idprodotto integer,
    idutente integer,
    valutazione integer,
    testo text,
    data text
);
    DROP TABLE public.recensione;
       public         heap r       postgres    false    5            �            1259    16671    recensione_id_seq    SEQUENCE     �   CREATE SEQUENCE public.recensione_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.recensione_id_seq;
       public               postgres    false    232    5            h           0    0    recensione_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.recensione_id_seq OWNED BY public.recensione.id;
          public               postgres    false    231            �            1259    16533    utente    TABLE     �   CREATE TABLE public.utente (
    id integer NOT NULL,
    nome character varying,
    cognome character varying,
    email character varying,
    password character varying NOT NULL,
    isadmin boolean NOT NULL
);
    DROP TABLE public.utente;
       public         heap r       postgres    false    5            �            1259    16538    utente_id_seq    SEQUENCE     �   CREATE SEQUENCE public.utente_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.utente_id_seq;
       public               postgres    false    225    5            i           0    0    utente_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.utente_id_seq OWNED BY public.utente.id;
          public               postgres    false    226            �           2604    16711    carrello id    DEFAULT     j   ALTER TABLE ONLY public.carrello ALTER COLUMN id SET DEFAULT nextval('public.carrello_id_seq'::regclass);
 :   ALTER TABLE public.carrello ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    235    236    236            �           2604    16666    categoria id    DEFAULT     l   ALTER TABLE ONLY public.categoria ALTER COLUMN id SET DEFAULT nextval('public.categoria_id_seq'::regclass);
 ;   ALTER TABLE public.categoria ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    229    230    230            �           2604    16694    dettagliordini id    DEFAULT     v   ALTER TABLE ONLY public.dettagliordini ALTER COLUMN id SET DEFAULT nextval('public.dettagliordini_id_seq'::regclass);
 @   ALTER TABLE public.dettagliordini ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    234    233    234            �           2604    16602    disponibilita id    DEFAULT     t   ALTER TABLE ONLY public.disponibilita ALTER COLUMN id SET DEFAULT nextval('public.disponibilita_id_seq'::regclass);
 ?   ALTER TABLE public.disponibilita ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    218    217            �           2604    16603    indirizzo id    DEFAULT     l   ALTER TABLE ONLY public.indirizzo ALTER COLUMN id SET DEFAULT nextval('public.indirizzo_id_seq'::regclass);
 ;   ALTER TABLE public.indirizzo ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    220    219            �           2604    16604    metododipagamento id    DEFAULT     |   ALTER TABLE ONLY public.metododipagamento ALTER COLUMN id SET DEFAULT nextval('public.metododipagamento_id_seq'::regclass);
 C   ALTER TABLE public.metododipagamento ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    222    221            �           2604    16631 	   ordine id    DEFAULT     f   ALTER TABLE ONLY public.ordine ALTER COLUMN id SET DEFAULT nextval('public.ordine_id_seq'::regclass);
 8   ALTER TABLE public.ordine ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    227    228    228            �           2604    16605    prodotto id    DEFAULT     j   ALTER TABLE ONLY public.prodotto ALTER COLUMN id SET DEFAULT nextval('public.prodotto_id_seq'::regclass);
 :   ALTER TABLE public.prodotto ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    224    223            �           2604    16675    recensione id    DEFAULT     n   ALTER TABLE ONLY public.recensione ALTER COLUMN id SET DEFAULT nextval('public.recensione_id_seq'::regclass);
 <   ALTER TABLE public.recensione ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    232    231    232            �           2604    16606 	   utente id    DEFAULT     f   ALTER TABLE ONLY public.utente ALTER COLUMN id SET DEFAULT nextval('public.utente_id_seq'::regclass);
 8   ALTER TABLE public.utente ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    226    225            W          0    16708    carrello 
   TABLE DATA                 public               postgres    false    236   Jf       Q          0    16663 	   categoria 
   TABLE DATA                 public               postgres    false    230   �f       U          0    16691    dettagliordini 
   TABLE DATA                 public               postgres    false    234    h       D          0    16509    disponibilita 
   TABLE DATA                 public               postgres    false    217   �h       F          0    16513 	   indirizzo 
   TABLE DATA                 public               postgres    false    219   dj       H          0    16517    metododipagamento 
   TABLE DATA                 public               postgres    false    221   3k       O          0    16628    ordine 
   TABLE DATA                 public               postgres    false    228   �k       J          0    16524    prodotto 
   TABLE DATA                 public               postgres    false    223    m       S          0    16672 
   recensione 
   TABLE DATA                 public               postgres    false    232   �u       L          0    16533    utente 
   TABLE DATA                 public               postgres    false    225   v       j           0    0    carrello_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.carrello_id_seq', 13, true);
          public               postgres    false    235            k           0    0    categoria_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.categoria_id_seq', 8, true);
          public               postgres    false    229            l           0    0    dettagliordini_id_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.dettagliordini_id_seq', 17, true);
          public               postgres    false    233            m           0    0    disponibilita_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.disponibilita_id_seq', 47, true);
          public               postgres    false    218            n           0    0    indirizzo_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.indirizzo_id_seq', 6, true);
          public               postgres    false    220            o           0    0    metododipagamento_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.metododipagamento_id_seq', 35, true);
          public               postgres    false    222            p           0    0    ordine_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.ordine_id_seq', 13, true);
          public               postgres    false    227            q           0    0    prodotto_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.prodotto_id_seq', 16, true);
          public               postgres    false    224            r           0    0    recensione_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.recensione_id_seq', 2, true);
          public               postgres    false    231            s           0    0    utente_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.utente_id_seq', 6, true);
          public               postgres    false    226            �           2606    16713    carrello carrello_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.carrello
    ADD CONSTRAINT carrello_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.carrello DROP CONSTRAINT carrello_pkey;
       public                 postgres    false    236            �           2606    16670    categoria categoria_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.categoria
    ADD CONSTRAINT categoria_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.categoria DROP CONSTRAINT categoria_pkey;
       public                 postgres    false    230            �           2606    16696 "   dettagliordini dettagliordini_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.dettagliordini
    ADD CONSTRAINT dettagliordini_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.dettagliordini DROP CONSTRAINT dettagliordini_pkey;
       public                 postgres    false    234            �           2606    16550     disponibilita disponibilita_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.disponibilita
    ADD CONSTRAINT disponibilita_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.disponibilita DROP CONSTRAINT disponibilita_pkey;
       public                 postgres    false    217            �           2606    16552    indirizzo indirizzo_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.indirizzo
    ADD CONSTRAINT indirizzo_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.indirizzo DROP CONSTRAINT indirizzo_pkey;
       public                 postgres    false    219            �           2606    16554 (   metododipagamento metododipagamento_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.metododipagamento
    ADD CONSTRAINT metododipagamento_pkey PRIMARY KEY (id);
 R   ALTER TABLE ONLY public.metododipagamento DROP CONSTRAINT metododipagamento_pkey;
       public                 postgres    false    221            �           2606    16634    ordine ordine_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.ordine
    ADD CONSTRAINT ordine_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.ordine DROP CONSTRAINT ordine_pkey;
       public                 postgres    false    228            �           2606    16556    prodotto prodotto_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.prodotto
    ADD CONSTRAINT prodotto_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.prodotto DROP CONSTRAINT prodotto_pkey;
       public                 postgres    false    223            �           2606    16679    recensione recensione_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.recensione
    ADD CONSTRAINT recensione_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.recensione DROP CONSTRAINT recensione_pkey;
       public                 postgres    false    232            �           2606    16560    utente utenti_pk 
   CONSTRAINT     N   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utenti_pk PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.utente DROP CONSTRAINT utenti_pk;
       public                 postgres    false    225            �           2606    16719 !   carrello carrello_idprodotto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.carrello
    ADD CONSTRAINT carrello_idprodotto_fkey FOREIGN KEY (idprodotto) REFERENCES public.prodotto(id);
 K   ALTER TABLE ONLY public.carrello DROP CONSTRAINT carrello_idprodotto_fkey;
       public               postgres    false    236    4762    223            �           2606    16714    carrello carrello_idutente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.carrello
    ADD CONSTRAINT carrello_idutente_fkey FOREIGN KEY (idutente) REFERENCES public.utente(id);
 I   ALTER TABLE ONLY public.carrello DROP CONSTRAINT carrello_idutente_fkey;
       public               postgres    false    236    4764    225            �           2606    16697 +   dettagliordini dettagliordini_idordine_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.dettagliordini
    ADD CONSTRAINT dettagliordini_idordine_fkey FOREIGN KEY (idordine) REFERENCES public.ordine(id);
 U   ALTER TABLE ONLY public.dettagliordini DROP CONSTRAINT dettagliordini_idordine_fkey;
       public               postgres    false    228    234    4766            �           2606    16702 -   dettagliordini dettagliordini_idprodotto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.dettagliordini
    ADD CONSTRAINT dettagliordini_idprodotto_fkey FOREIGN KEY (idprodotto) REFERENCES public.prodotto(id);
 W   ALTER TABLE ONLY public.dettagliordini DROP CONSTRAINT dettagliordini_idprodotto_fkey;
       public               postgres    false    223    4762    234            �           2606    16576    disponibilita idprodottofk    FK CONSTRAINT        ALTER TABLE ONLY public.disponibilita
    ADD CONSTRAINT idprodottofk FOREIGN KEY (idprodotto) REFERENCES public.prodotto(id);
 D   ALTER TABLE ONLY public.disponibilita DROP CONSTRAINT idprodottofk;
       public               postgres    false    223    4762    217            �           2606    16581 !   indirizzo indirizzo_idutente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.indirizzo
    ADD CONSTRAINT indirizzo_idutente_fkey FOREIGN KEY (idutente) REFERENCES public.utente(id);
 K   ALTER TABLE ONLY public.indirizzo DROP CONSTRAINT indirizzo_idutente_fkey;
       public               postgres    false    225    4764    219            �           2606    16586 1   metododipagamento metododipagamento_idutente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.metododipagamento
    ADD CONSTRAINT metododipagamento_idutente_fkey FOREIGN KEY (idutente) REFERENCES public.utente(id);
 [   ALTER TABLE ONLY public.metododipagamento DROP CONSTRAINT metododipagamento_idutente_fkey;
       public               postgres    false    4764    221    225            �           2606    16640 $   ordine ordine_idmetodopagamento_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.ordine
    ADD CONSTRAINT ordine_idmetodopagamento_fkey FOREIGN KEY (idmetodopagamento) REFERENCES public.metododipagamento(id);
 N   ALTER TABLE ONLY public.ordine DROP CONSTRAINT ordine_idmetodopagamento_fkey;
       public               postgres    false    221    228    4760            �           2606    16635    ordine ordine_idutente_fkey    FK CONSTRAINT     |   ALTER TABLE ONLY public.ordine
    ADD CONSTRAINT ordine_idutente_fkey FOREIGN KEY (idutente) REFERENCES public.utente(id);
 E   ALTER TABLE ONLY public.ordine DROP CONSTRAINT ordine_idutente_fkey;
       public               postgres    false    228    225    4764            �           2606    16725 "   prodotto prodotto_idcategoria_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.prodotto
    ADD CONSTRAINT prodotto_idcategoria_fkey FOREIGN KEY (idcategoria) REFERENCES public.categoria(id);
 L   ALTER TABLE ONLY public.prodotto DROP CONSTRAINT prodotto_idcategoria_fkey;
       public               postgres    false    4768    230    223            �           2606    16680 %   recensione recensione_idprodotto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.recensione
    ADD CONSTRAINT recensione_idprodotto_fkey FOREIGN KEY (idprodotto) REFERENCES public.prodotto(id);
 O   ALTER TABLE ONLY public.recensione DROP CONSTRAINT recensione_idprodotto_fkey;
       public               postgres    false    232    4762    223            �           2606    16685 #   recensione recensione_idutente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.recensione
    ADD CONSTRAINT recensione_idutente_fkey FOREIGN KEY (idutente) REFERENCES public.utente(id);
 M   ALTER TABLE ONLY public.recensione DROP CONSTRAINT recensione_idutente_fkey;
       public               postgres    false    225    4764    232            W   �   x���v
Q���W((M��L�KN,*J���Ws�	uV�04�Q0�Q0�Q 2�s�Su�#|�uJ�JS5��<�1�l�)�!�$�a6��0�HS, � y�b=��C�ƈl3,�f�$`CLp� �҇i      Q   /  x���MK1���s�B�<�RdA+���$�q�dI& ��N�ڃ���¾y�̼i����fѾ���%sac�)�����Wpv9��"y�c�x�ӆ A�1J@\ɜ?���U���7<=��5u����7�$����T{mgպkQ��7f�sR�iC��[�8�n��S���
t>Pk�r���ue��@�&��uy�5�h-�6����L_���Ȍ2���ݛ�»3����({�2D`2�:�叨�D֢�&��ƕ5�������.]+x�{I2���߻�K�Ƈ�����|����~ K~�      U   �   x����
�0�y�=*,�&�œ�R�V�j�����w����Gn�$dl�5�l����M��y�>&�����?��V����\���:3&�#�r�R�@�UR�D�Q!����3"�� ���-B�d$��\ɵxm��,�\�e)u�dt�GxW��)�S��U�~��      D   y  x����j1�O��-,e'�M6�ԃ��Z�Z��� *����d&��~>����|��b���m�b�y5���y<>�Ə��2����7�/�v�6�1�6f��6��'��BN�Kp��&8��7�On��[k��>gk����^k�`�E������ezHZ=��NS0�2�'�:1�I\L2@�k���i�B�`��H��4J��P��^�˰L1����9E�)�dNi�0���k�0��no/�q�t�����s��h���-N{����4Jj�D�v����Y��F�ũ��C�uu�-nY&T���䢬H��0�#r�����-�>�zCQ{���j�whn����`�v绊�ǒ�|v�K'�p�z͕�Bo�&WϋQ��p�ܮ�N&?��4      F   �   x���v
Q���W((M��L���K�,ʬ��Ws�	uV�0�QP�K����S�,@�sF&�ma`f�G���ĜĤ��D �TG���4UӚ˓+�����V$�"ےX�X��������WR��Y�\��t��6�� ��_�Z�X��� �
2}f	<�!��,�|f:
i�9�D�bJ[���d4��� 6O�6      H   �   x���v
Q���W((M��L��M-�O�O�,HLO�M�+�Ws�	uV�0�QPwN,*ITH�TH.JM�,�W��e&*��d�xa��� � ������ Lc e��PRT��i��I�K�MIr�!�;��)f�9ňJ�b�⒴Ĝb�S�� �q�      O   3  x��ԻJA�>O1]��\���X���B܅d���Q|'I#����0g����?M����iھ3_���'����{1w��ޙ3\Y�%�xMl��X�!�e~lڇzS]v����:�0����Ţ�Ӡ�����@��0�D>�,�CT�F��iD�3�����MA�(���@<�7�*�(���r�)D��(�B���c�$�4����T�i
��p%A�a��?2�T�� �R���s�����?������:(W]{]oo���7
'���/�P��9K�T�Q�%��7Z�<�      J   k  x��W�n��}߯臅� �xEM`,l��x&�e;���fS�5��t7�#�ɏ��I�2�A�H��Ed��ԩs�/>�N�o�ŧ����/jɧ�V��V����ۓ�C2a����P(e,��}�qX��}Z�1̂i�����+��me�f\��
ުZ�%�#�L'4k��(|����L0-�ҭb�d����R@-�\i#��Yݷ�~D퓘b �F��7�v����)�@��	_6����YL�z�&��CU�2��畬��������"�yV�$[����,�����"NS�8�(���`��~�h�aNq����oݚ���~���&��5�v��.,�yD	a�7����kW��X�񥾪$�P#T�����$UK�ҭ�"����X��R������r�Uk�$��2.&.{�����_Y����i��O*�����O;��~��
J�m=�\�����1�Lj���V��|Da��_��rr�W���v�^�-�!�@�мk�!ƄW�#�)��萭�� [m���彠�5��pOg�WԪ岕��K��L�F�[&M'5�g@��#Z+v�׵��a����{�@EX�z�lz�2�jY�0S�4�A6���6@�Y̭�x�m�__�a��C���+�HE���������[D)xIPT1CȒ��C�^A�{�-A�x����!Q#/��ߔ.���<3���w�\7�gS�F�X�9fx���
��^W�Y͝"��)2����r"�:A:қ����5f��?���4e�^!��:������;BS��k��������_��K��yE��^�uVAT&>-�U���@hx��y��oH��@=M'=��=���H\�G�qr4���~���]�� W)�p�mK�>��C�gZ"(T�h�Ù΁��i�&v����(^	����R�JI�%�F�Jh�7T���0eK躞s��k��vq�(|�J�$�w4����=5n>H�����w�n�q:Ӆ���t�����������#��X�{��=��p�� e�sm�ٞ�*Ł�`��=�j'��+���
�o�Q��4��y0�=#�ݘ���0���P�Ukإ�u/�g�z�K�$�V�jT1�ܞ��oD�Q�$>I�vz�����ֲ_]{΍U�c	����˻gT�rV�I�&A|C�A �"AKv�=��./s�5���o��<{Ǹw��Ёأ]�����.�C#�Wi�p��;c��2bԪ�pȑ&iLDZ�v���3S��۟��(e��r�g7?�z��h؃�#qu���[���}�<>>N�!W��q쏇�?:?��_]���K��^>��A�|�_I+���e�
�=���
���B"\�y��%&({n���0gq��#���S�����Ȥ�-�Wԥt:�Y�و�Za,���`�}C�tA��3t�1H�I�tj+p�z�C�-�Hˀk������#���^8�۴_^�yZ�蹧z\~��� �F�w��0�QS�8�`b'_p��UL � 5?��n�)p��wB�t�����N�_�a���w��X���0q�l��v=CH7
[���$I��L���-�ㄊ��R���0)���:���.ON�s4C���i�}A(Ƶg�bXd͎��w���٥�,�sp�Q�x��)�W�[�I�!��N�V��=��,q����t��Hs3�b����#�� ��Y�p�ZՌ<}G�����)�*����iv�g!�w��#�vF�v�G���V�l��ݿF�����#Fg=�ҵ��"ޙ�dS�g����*ٿ%�����-�3ٌfR�������{wgJ�1r�㦿�ƭ �����"Ky0�E!D��!�G�X̡�S?�c?�� ע|��tWhi��a�	Q�sQ7b�����>z�J�����ď��	�EK·�{@`d��}��Z9�^�\Ҟ��6��&6��0p�!~/��>��"I���y�gQV����y�������|W�iy��ƍd���Gr�������R�7N�^Y�en�ոG����1���}<n3�W%���;C��Ï݈b48m�w�^��?c=��� �Y��|�[z��y!�XƳ��"��O�aC�      S   d   x���v
Q���W((M��L�+JMN�+���KUs�	uV�0�Q0�Q0��N�99�
�ɉE��@�������������5�'Q��Y\����T.. .s      L   �   x��νn�0�=O�!TB!�jԥm�d���l�����$�<=��Tu�����"�KD�e��m�@x�4_�A���m\���M
-�On!��ۏ��=/��	}ksw�����-�ڨ�~w	>ޚ�X@NbE�-`CXm,h�,�<�8/�ꛇg���"�~L ���/�X��I�UnY1��~Wy"����[1ѬdC�����,v}z�r�+�'\�     