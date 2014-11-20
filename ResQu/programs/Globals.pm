package Globals;
use DBI;
use File::Spec;


my $dbh = DBI->connect('DBI:mysql:semmedVER23;host=127.0.0.1', 'semmeduser', 'semmeduser')
  or die "Couldn't connect to database: " . DBI->errstr;

my $sth = $dbh->prepare('SELECT NOVEL FROM (SELECT CONCEPT_ID FROM CONCEPT WHERE CUI = ?) C, CONCEPT_SEMTYPE CS WHERE CS.CONCEPT_ID=C.CONCEPT_ID')
  or die "Couldn't prepare statement: " . $dbh->errstr;

1;


#returns -1, 0, 1 when the concept is not novel, not found, novel
sub DBCheck(){
  my ($cui) = (@_);
  my @data;
  my $novelty;
  $sth->execute($cui)
    or die "Couldn't execute statement: " . $sth->errstr;
  if (@data = $sth->fetchrow_array()){
    $novelty = $data[0];
  }else{
    $novelty="U";
  }
  $sth->finish;

  if ($novelty eq "Y"){
    return 1;
  }elsif ($novelty eq "N"){
    return -1;
  }else{
    return 0;
  }
}

##########################################################################
# ExtractConcepts: Extract the best concept from the compound term.
##########################################################################
sub ExtractConcepts {
  my ($conc1, $conc2 ) = @_;
  my ($outconc);
  if ( $conc1 eq "" ) { return $conc2; }
  else { return $conc1; }
}


##########################################################################
# GetRelations: Create a hash of relations.
# Key is sentence_id, value is an array of relations of the sentence.
##########################################################################
sub GetRelations {
  my ($filename) = @_;

  my $format = &GetFormat($filename);

  undef %relation_hash;
  $previous_id = 0;
  $pmid        = 0;
  undef @rel_arr;
  open( F, "$filename" ) || die "can't open $filename\n";
  while (<F>) {
    my ($line) = $_;
    chomp($line);
    if ( $line =~ /\|relation\|/ ) {
      $line = &Translate($line) unless $format;

      @rel_elements = split( /\|/, $line );
      $pmid = $rel_elements[0];
      unless ( $previous_id eq $pmid ) {
	$relation_hash{$previous_id} = [@rel_arr];
	undef @rel_arr;
      }
      push @rel_arr, $line;
      $previous_id = $pmid;
    }
  }
  $relation_hash{$pmid} = [@rel_arr];
  close(F);
  return %relation_hash;
}

##########################################################################
# GetSentences: Create a hash of sentences.
# Key is sentence_id, value is the entire sentence line.
##########################################################################
sub GetSentences {
  my ($filename) = @_;
  undef %sentence_hash;
  open( F, "$filename" ) || die "can't open $filename\n";
  while (<F>) {
    my ($line) = $_;
    chomp($line);
    if ( $line =~ /^([\w\d]+)\.(ti|ab)\.(\d+)\s/ ) {
      unless ( $line =~ /$$$$ FOUND/ ) {
	$sentence           = $line;
	$id                 = "$1.$2.$3";
	$sentence_hash{$id} = $sentence;
      }
    }
  }
  close(F);
  return %sentence_hash;
}


##########################################################################
# GetNovelConcepts: Read adhoc novelty concepts from a text file. These
# should eventually handled with rules.
##########################################################################
sub GetNovelConcepts {
	my ($filename) = @_;
	@concs = ();
	open( F, "$filename" ) || die "can't open $filename\n";
	while (<F>) {
		my ($line) = $_;
		chomp($line);
		unless ( $line =~ /^#/ ) {
			push @concs, $line;
		}
	}
	close(F);
	return @concs;
}

###########################################################################
# PreFilter: A preliminary filter that takes care of circular arguments and
# empty arguments (None.)
# Takes in a hash of relations, and returns a hash of filtered relations.
##########################################################################
sub PreFilter {
  my ($in_rel) = @_;
  my (%rels)   = %$in_rel;
  undef %pre_filter_hash;
  undef @rel_arr;
  foreach $pmid ( keys %rels ) {
    @rel_arr = @{ $rels{$pmid} };
    undef @pre_filter_arr;
    foreach $rel (@rel_arr) {
      @rel_elements = split( /\|/, $rel );

      $arg1 = &ExtractConcepts( $rel_elements[3],  $rel_elements[7] );
      $arg2 = &ExtractConcepts( $rel_elements[10], $rel_elements[14] );

      #remove circular
      unless ( $arg1 eq $arg2 ) {
	push @pre_filter_arr, $rel;
      }
    }

    if ( $#pre_filter_arr > -1 ) {
      $pre_filter_hash{$pmid} = [@pre_filter_arr];
    }
  }
  return %pre_filter_hash;
}


##################################################################################
# InNoveltyList: Check if a concept is in adhoc novelty list.
##################################################################################
sub InNoveltyList {
  my (@novel_concepts,$arg) = @_;
  my $arg1 = &ExtractConcepts( $arg, "" );
  foreach (@novel_concepts) {
    if ( $arg1 eq $_ ) { return 1; }
  }
  return 0;
}

####################################################################################
# SortHash : Sorts a hash of predications, first by PMID, then by ti, ab, then by
# sentence number. Written to restore the order of citations in the input file, since
# this is overridden when the sentences and relations are put into hashes.
####################################################################################
sub SortHash {
  @a_fields = split( /\./, $a );
  @b_fields = split( /\./, $b );
  $b_fields[0] <=> $a_fields[0]
    || $b_fields[1] cmp $a_fields[1]
      || $a_fields[2] <=> $b_fields[2];
}

####################################################################################
# PrintHash : Prints a hash of predications. Adds sentences, PMIDs  and does sorting.
####################################################################################
sub PrintHash {
  my ( $hash, $outfile_in, %sent_hash ) = @_;
  %hash_to_print = %$hash;
  $outfile       = $outfile_in;
  $previd        = 0;

  #($volume,$dir,$file) = File::Spec->splitpath( $outfile );
  #$x=File::Spec->catfile($volume, $dir, $domain . "_" . $file);


  open( FF, ">$outfile_in" ) || die "cannot open $outfile_in";
  foreach $id ( sort SortHash keys %hash_to_print ) {
    $id =~ /^([\w\d]+)\./;
    $pmid = $1;
    unless ( $pmid eq $previd ) {
      print FF "----- Citation $pmid -----\n\n";
    }
    $sentence = $sent_hash{$id};
    print FF "$sentence\n\n";
    @rels = @{ $hash_to_print{$id} };
    foreach (@rels) {
      print FF "$_\n";
    }
    print FF "\n";
    $previd = $pmid;File::Spec->splitpath( $path );
  }
  close(FF);
}



sub GetFormat() {
  my ($fileName) = @_;
  open FILE, "$fileName" or die "Couldn't open $fileName\n";

  while (<FILE>){
    if (/\|relation\|/){
      my @arr = split /\|/;
      my $length = @arr;
      if ($length==11){
	print "Input file is in the old format.\n";
	return 0;
      }elsif ($length==14 || $length==15){
	print "Input file is in the new format.\n";
	return 1;
      }
    }
  }
  close FILE;
}


sub Translate(){
  my ($line) = @_;
  my @arrOld = split /\|/, $line;

  my @arrNew = ($arrOld[0],$arrOld[1],$arrOld[5],$arrOld[2],$arrOld[3],$arrOld[4],"",
		"",$arrOld[6],$arrOld[10],$arrOld[7],$arrOld[8],$arrOld[9],"","");

  $" = "|";
  return "@arrNew";
}
